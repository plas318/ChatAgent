package com.example.chatagent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


// Interface logic, where fetch, update, crud .. etc..

@Dao
interface MessageDao {
    // Query fetch all messages by time (max 20 messages).
    @Query("SELECT * from messages order by date asc limit 20")
    fun getMessages(): LiveData<List<Message>>


    @Insert
    suspend fun insertMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)


    @Query("DELETE from messages")
    suspend fun deleteAllMessage()
}