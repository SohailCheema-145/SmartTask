package com.tcp.smarttasks.domain.usecase

import com.tcp.smarttasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksForSpecificDateUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(date: String) = repository.getTasksForSpecificDate(date)
}
