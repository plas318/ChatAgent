package com.example.chatagent

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/// Note: ViewModel provides data to ui,and sync with ui layer and model

class MessageViewModel(application: Application, private val apiKey: String) : AndroidViewModel(application) {
    private val db: MessageDatabase = MessageDatabase.getDatabase(application)
    private val messageDao = db.messageDao()

    val messages: LiveData<List<Message>> = messageDao.getMessages()

    // Dao, handle Message Logics

    fun sendMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.insertMessage(message)
        }
    }

    fun deleteMessage(message: Message){
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.deleteMessage(message)
        }
    }

    fun deleteAllMessage(){
        viewModelScope.launch(Dispatchers.IO) {
            messageDao.deleteAllMessage()
        }
    }

    // API requests and logics
    // TODO create and use a Repository for better practice

    private val gptApiService = RetrofitInstance.apiService

    fun getGPTResponse(prompt: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = gptApiService.askGPT(
                "Bearer $apiKey", // api key
                RequestMessage(model="gpt-3.5-turbo", messages = listOf(

                    // Customize this part to customize the assistant, role, etc..
                    GptMessages(
                        "system",
                        "You are a loyal servant to your master"
                    ),
                    GptMessages(
                        role = "user",
                        content = prompt
                    )

                )) // more tokens cost more..
            )

            if (response.isSuccessful) {
                var message: Message = if (response.body()!!.choices[0].finish_reason != "stop")
                {
                    Message(0, content = response.body()!!.choices[0].finish_reason,
                        timestamp = System.currentTimeMillis(), type="system")
                }
                else {
                    Message(0, content = response.body()!!.choices[0].message.content,
                        timestamp = System.currentTimeMillis(), type="system")
                }

                sendMessage(message)

            } else {
                // Bad request, etc.. handle logic
            }
        }
    }

}
