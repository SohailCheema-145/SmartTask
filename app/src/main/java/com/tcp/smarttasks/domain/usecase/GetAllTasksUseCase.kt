package com.tcp.smarttasks.domain.usecase

import com.tcp.smarttasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getAllTasks()
}
