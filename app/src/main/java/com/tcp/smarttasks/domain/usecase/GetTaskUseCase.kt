package com.tcp.smarttasks.domain.usecase

import com.tcp.smarttasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(id: String) = repository.getTask(id)
}
