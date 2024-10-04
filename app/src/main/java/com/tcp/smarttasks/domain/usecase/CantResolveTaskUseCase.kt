package com.tcp.smarttasks.domain.usecase

import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.domain.repository.TaskRepository
import javax.inject.Inject

class CantResolveTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(task: Task) = repository.cantResolveTask(task)
}
