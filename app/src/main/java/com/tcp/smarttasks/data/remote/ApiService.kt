package com.tcp.smarttasks.data.remote

import com.tcp.smarttasks.data.remote.model.TasksResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getTasks(): TasksResponse
}
