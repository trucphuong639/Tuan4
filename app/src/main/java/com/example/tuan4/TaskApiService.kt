package com.example.tuan4

import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Path

interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): ApiResponseList // 🔥 Chỉ giữ lại kiểu trả về

    @GET("task/{id}")
    suspend fun getTaskDetail(@Path("id") id: Int): Task

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}
