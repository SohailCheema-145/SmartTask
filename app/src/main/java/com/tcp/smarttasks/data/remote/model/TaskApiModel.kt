package com.tcp.smarttasks.data.remote.model

import com.google.gson.annotations.SerializedName


data class TaskApiModel(
    @SerializedName("id")
    val id: String?,
    @SerializedName("TargetDate")
    val targetDate: String?,
    @SerializedName("DueDate")
    val dueDate: String?,
    @SerializedName("Title")
    val title: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Priority")
    val priority: Int?,
)
