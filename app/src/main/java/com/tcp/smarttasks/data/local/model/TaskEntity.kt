package com.tcp.smarttasks.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tcp.smarttasks.domain.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val targetDate: String?,
    val dueDate: String?,
    val title: String?,
    val description: String?,
    val priority: Int = 0,
    val status: TaskStatus = TaskStatus.UNRESOLVED,
    var comment: String = ""
)
