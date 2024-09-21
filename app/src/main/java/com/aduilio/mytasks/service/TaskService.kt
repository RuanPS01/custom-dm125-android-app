package com.aduilio.mytasks.service

import android.util.Log
import com.aduilio.mytasks.entity.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskService {

    private val taskRepository = RetrofitService().getTaskRepository()

    fun create(task: Task) {
        taskRepository.create(task).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {

                } else {
                    Log.e("server", "Erro do servidor")
                    response.errorBody()?.let { errorBody ->
                        Log.e("server", errorBody.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("server", "Erro do servidor")
                t.message?.let { exception ->
                    Log.e("server", "Server exception: $exception")
                }
            }
        })
    }
}