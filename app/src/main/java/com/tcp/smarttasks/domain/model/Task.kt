package com.tcp.smarttasks.domain.model

data class Task(
    val id: String,
    val targetDate: String?,
    val dueDate: String?,
    val title: String?,
    val description: String?,
    val priority: Int = 0,
    val status: TaskStatus = TaskStatus.UNRESOLVED,
    var comment: String = ""
)