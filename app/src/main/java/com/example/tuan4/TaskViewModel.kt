package com.example.tuan4

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Định nghĩa API
interface TaskApi {
    @GET("tasks") // 🔥 API để lấy danh sách công việc
    suspend fun getTasks(): ApiResponseList // 🔥 Đổi kiểu trả về thành ApiResponseList
}

// Khởi tạo Retrofit
private val retrofit = Retrofit.Builder()
    .baseUrl("https://amock.io/api/researchUTH/") // 🔥 URL API
    .addConverterFactory(GsonConverterFactory.create())
    .build()
private val api = retrofit.create(TaskApi::class.java)

class TaskViewModel : ViewModel() {
    var taskList = mutableStateListOf<Task>()
        private set

    init {
        fetchTasks() // 🔥 Gọi API khi ViewModel được tạo
    }

    fun fetchTasks() {
        viewModelScope.launch {
            try {
                val response = api.getTasks() // 🔥 Gọi API lấy danh sách

                println("✅ API Response: $response")

                if (response.isSuccess) { // 🔥 Kiểm tra phản hồi thành công
                    taskList.clear()
                    taskList.addAll(response.data)
                } else {
                    println("❌ API Error: ${response.message}")
                    taskList.clear()
                }
            } catch (e: Exception) {
                println("❌ API Exception: ${e.message}")
                taskList.clear()
            }
        }
    }
}

// Dữ liệu API trả về
data class ApiResponseList(
    val isSuccess: Boolean,
    val message: String,
    val data: List<Task>
)
