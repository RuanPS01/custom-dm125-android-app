package com.ruan.mytasks.service

import com.ruan.mytasks.adapter.LocalDateAdapter
import com.ruan.mytasks.adapter.LocalTimeAdapter
import com.ruan.mytasks.repository.TaskRepository
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime

class RetrofitService {

    private var taskRepository: TaskRepository

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://custom-dm125-to-do-app-server.onrender.com/")
                .client(configureClient())
                .addConverterFactory(configureConverter())
                .build()

        taskRepository = retrofit.create(TaskRepository::class.java)
    }

    fun getTaskRepository() = taskRepository

    private fun configureClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
    }

    private fun configureConverter(): Converter.Factory {
        val gson = GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
                .create()

        return GsonConverterFactory.create(gson)
    }
}