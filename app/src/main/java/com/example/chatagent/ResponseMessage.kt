package com.example.chatagent

/* Example Response
"choices": [
{
    "finish_reason": "stop",
    "index": 0,
    "message": {
    "content": "The 2020 World Series was played in Texas at Globe Life Field in Arlington.",
    "role": "assistant"
    },
    "logprobs": null
}
],
"created": 1677664795,
"id": "chatcmpl-7QyqpwdfhqwajicIEznoc6Q47XAyW",
"model": "gpt-3.5-turbo-0613",
"object": "chat.completion",
"usage": {
    "completion_tokens": 17,
    "prompt_tokens": 57,
    "total_tokens": 74
}
*/

data class ResponseMessage (
    val created: Int,
    val choices: List<Choice>
)

data class Choice(
    val message: GptMessage,
    val index: Int,
    val finish_reason: String
)

data class GptMessage(
    val content: String,
    val role: String
)