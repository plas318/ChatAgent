package com.example.chatagent
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.*

interface GptAPIService {
    // OpenAi Endpoint for GPT 3.5, 4.0, turbo
    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun askGPT(
        @Header("Authorization") authHeader: String,
        @Body requestBody: RequestMessage
    ): Response<ResponseMessage>

}