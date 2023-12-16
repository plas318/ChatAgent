package com.example.chatagent

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatagent.databinding.ActivityMainBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MessageViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        // TODO Add a settings activity to handle Aquire API key logic and etc..
        if (isFirstLaunch()){
            promptApiKey()
        }

        val apiKey = getApiKey()
        val factory = ViewModelFactory(application, apiKey)
        viewModel = ViewModelProvider(this, factory).get(MessageViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatAdapter = ChatAdapter()
        recyclerView.adapter = chatAdapter
        recyclerView.addItemDecoration((VerticalSpaceItemDecoration(20)))

        viewModel.messages.observe(this, Observer { messages ->
            try {
                chatAdapter.setList(messages)
            } catch (e: Exception) {
                // handle exceptions
            }
        })

        registerForContextMenu(binding.chatRecyclerView)

        // Setup Android Toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Bind SendBtn
        binding.sendButton.setOnClickListener {
            val msg: String = binding.messageInput.text.toString()

            if (msg.isNotEmpty()) {
                insertMessage(msg)
                binding.messageInput.setText("")
            } else {
                //alert user
            }
        }


    }

    // API_KEY Functions //
    private fun isFirstLaunch(): Boolean {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("api_key", null) == null
    }

    private fun promptApiKey() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Enter Your (OpenAI) API Key")
            .setView(input)
            .setPositiveButton("OK") { dialog, _ ->
                val apiKey = input.text.toString()
                saveApiKey(apiKey)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun saveApiKey(apiKey: String){
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("api_key", apiKey).apply()
    }

    private fun getApiKey(): String {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("api_key", "") ?: ""
    }
    //


    // Message Functions //
    private fun insertMessage(msg: String) {
        val newMessage = Message(
            0,
            content = msg,
            timestamp = System.currentTimeMillis().toLong(),
            type = "user"
        )
        // Save to db
        viewModel.sendMessage(newMessage)

        // Send API Request to openAI
        viewModel.getGPTResponse(msg)


        // TODO Add Feedback using toast or other methods
        //Toast.makeText(requireContext()"Successfully add message", Toast.LENGTH_LONG).show()
    }

    private fun deleteMessage(message: Message) {
        viewModel.deleteMessage(message)
    }

    private fun deleteAllMessage() {
        viewModel.deleteAllMessage()
    }
    //

    //    Top Right Options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                    showConfirmationDialog("delete all messages?") {
                        deleteAllMessage()
                    }
                    return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //


    //Context Menu inside a recyclerview for each Item in the list)
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.message_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val message = chatAdapter.messages[chatAdapter.currentPosition]

        when (item.itemId) {
            R.id.action_delete -> {
                // Handle 'Delete' action
                deleteMessage(message)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    // Dialog, modal to prompt
    private fun showConfirmationDialog(msg: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Action")
            .setMessage("Are you sure you want to ${msg}?")
            .setPositiveButton("Yes") { dialog, which ->
                onConfirm()
            }
            .setNegativeButton("No", null)
            .show()
    }

}


