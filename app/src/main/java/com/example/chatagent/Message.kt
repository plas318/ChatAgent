package com.example.chatagent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

// This is sql model (primary key, attributes, maybe functions)


@Entity(tableName="messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val content: String,
    @ColumnInfo(name="date")
    val timestamp: Long,
    val type: String // TODO Enum would be better, consists two types : {System, User}

)

