package com.tcp.smarttasks.domain.repository

import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<Resource<List<Task>>>
    fun getTasksForSpecificDate(date: String): Flow<Resource<List<Task>>>
    fun getTask(id: String): Flow<Resource<Task?>>
    fun resolveTask(task: Task): Flow<Resource<Task>>
    fun cantResolveTask(task: Task): Flow<Resource<Task>>
}
