package com.tcp.smarttasks.data.repository

import com.tcp.smarttasks.data.local.TaskDao
import com.tcp.smarttasks.data.mapper.toDomainModel
import com.tcp.smarttasks.data.mapper.toEntity
import com.tcp.smarttasks.data.remote.ApiService
import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.domain.model.TaskStatus
import com.tcp.smarttasks.domain.repository.TaskRepository
import com.tcp.smarttasks.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getAllTasks(): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Loading())
        try {
            //hit api call and map tasks
            val domainTasks = apiService.getTasks().tasks.map { it.toDomainModel() }

            // update or insert new tasks
            domainTasks.forEach { domainTask ->
                val existingTask = taskDao.getTaskById(domainTask.id)
                if (existingTask != null) {
                    // Update existing task
                    val updatedTask = existingTask.copy(
                        targetDate = domainTask.targetDate,
                        dueDate = domainTask.dueDate,
                        title = domainTask.title,
                        description = domainTask.description,
                        priority = domainTask.priority
                    )
                    taskDao.updateTask(updatedTask)
                } else {
                    // Insert new task
                    taskDao.insertTask(domainTask.toEntity())
                }

            }

            //get tasks from database and return
            taskDao.getAllTasks().collect { tasks ->
                val localTasks = tasks.map { it.toDomainModel() }

                //emit response
                emit(Resource.Success(localTasks))
            }
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }
    }

    override fun getTasksForSpecificDate(date: String): Flow<Resource<List<Task>>> = flow {
        emit(Resource.Loading())
        try {
            // Get cached tasks from the database and map them to the domain model
            val cachedTasks = taskDao.getTasksForDay(date).map { tasks ->
                tasks.map { it.toDomainModel() }
            }

            cachedTasks.collect { taskList ->
                emit(Resource.Success(sortTasks(taskList)))
            }
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }
    }

    override fun getTask(id: String): Flow<Resource<Task?>> = flow {
        emit(Resource.Loading())
        try {
            taskDao.getTaskFlowById(id).collect { taskEntity ->
                val taskDomain =
                    taskEntity?.toDomainModel()  // Assuming you have a mapping function
                emit(Resource.Success(taskDomain))
            }
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }
    }


    override fun resolveTask(task: Task): Flow<Resource<Task>> = flow {
        emit(Resource.Loading())
        try {
            // Update the task status to RESOLVED
            val resolvedTask =
                task.copy(status = TaskStatus.RESOLVED) // Ensure Task has a status field

            // Map the resolved Task to TaskEntity
            val taskEntity = resolvedTask.toEntity() // Implement toEntity() in your Task class

            // Save the updated TaskEntity in the database
            taskDao.updateTask(taskEntity)

            emit(Resource.Success(resolvedTask)) // Emit the resolved task
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}")) // Emit error if any exception occurs
        }
    }

    override fun cantResolveTask(task: Task): Flow<Resource<Task>> = flow {
        emit(Resource.Loading())
        try {
            // Update the task status to RESOLVED
            val resolvedTask =
                task.copy(status = TaskStatus.CANT_RESOLVE) // Ensure Task has a status field

            // Map the resolved Task to TaskEntity
            val taskEntity = resolvedTask.toEntity() // Implement toEntity() in your Task class

            // Save the updated TaskEntity in the database
            taskDao.updateTask(taskEntity)

            emit(Resource.Success(resolvedTask)) // Emit the resolved task
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}")) // Emit error if any exception occurs
        }
    }

    private fun sortTasks(tasks: List<Task>): List<Task> {
        return tasks.sortedWith(compareBy<Task> { it.priority }
            .thenBy { it.targetDate })
    }
}
