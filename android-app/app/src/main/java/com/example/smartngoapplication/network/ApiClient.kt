package com.example.smartngoapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // Default URL using USB ADB Reverse (127.0.0.1) or local Wi-Fi IP
    var baseUrl: String = "http://127.0.0.1:8080/"
        set(value) {
            field = if (value.endsWith("/")) value else "$value/"
            apiServiceInstance = createApiService(field)
        }

    private var apiServiceInstance: ApiService = createApiService(baseUrl)

    val apiService: ApiService
        get() = apiServiceInstance

    private fun createApiService(url: String): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
