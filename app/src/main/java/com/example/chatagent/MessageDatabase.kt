package com.example.chatagent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase


// android Room needs some initial setup,

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "chat_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}