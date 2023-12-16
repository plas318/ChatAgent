package com.example.chatagent

data class RequestMessage(
    val model: String,
    val messages: List<GptMessages>
)

data class GptMessages(
    val role: String,
    val content: String
)