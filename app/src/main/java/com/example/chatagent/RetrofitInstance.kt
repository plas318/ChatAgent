package com.example.chatagent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.math.log

object RetrofitInstance {
    private const val BASE_URL = "https://api.openai.com/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Set the logging level
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS) // Increase connection timeout
        .readTimeout(10, TimeUnit.SECONDS)    // Increase read timeout
        .writeTimeout(10, TimeUnit.SECONDS)   // Increase write timeout
        .build()

    val apiService: GptAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GptAPIService::class.java)
    }
}