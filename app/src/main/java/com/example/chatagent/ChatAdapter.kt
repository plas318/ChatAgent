package com.example.chatagent


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){
    var messages: List<Message> = listOf()
    var currentPosition: Int = -1

    // Implemented function that sets different layout based on the viewtype
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].type == "user") {
            R.layout.user_message
        } else {
            R.layout.system_message
        }
    }

    fun setList(newMessages: List<Message>){
        if (newMessages != this.messages){
            messages = newMessages
            notifyDataSetChanged()
        }
    }

    class ChatViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.setVariable(BR.message, item)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)

        return ChatViewHolder(binding).apply {
            // Add a right click(long click) listener to each msg item (delete)
            itemView.setOnLongClickListener{
                currentPosition = adapterPosition
                itemView.showContextMenu()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentMsg = messages[position]
        holder.bind(currentMsg)
    }





}