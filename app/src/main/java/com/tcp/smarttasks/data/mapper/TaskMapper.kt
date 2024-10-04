package com.tcp.smarttasks.data.mapper

import com.tcp.smarttasks.data.local.model.TaskEntity
import com.tcp.smarttasks.data.remote.model.TaskApiModel
import com.tcp.smarttasks.domain.model.Task

// Map API model to Domain model
fun TaskApiModel.toDomainModel(): Task = Task(
    id = id ?: "",
    targetDate = targetDate,
    dueDate = dueDate,
    title = title,
    description = description,
    priority = priority ?: 0
)

// Map Domain model to Entity (for local storage)
fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    targetDate = targetDate,
    dueDate = dueDate,
    title = title,
    description = description,
    priority = priority,
    status = status,
    comment = comment
)

// Map Entity to Domain model
fun TaskEntity.toDomainModel(): Task = Task(
    id = id,
    targetDate = targetDate,
    dueDate = dueDate,
    title = title,
    description = description,
    priority = priority,
    status = status,
    comment = comment
)
