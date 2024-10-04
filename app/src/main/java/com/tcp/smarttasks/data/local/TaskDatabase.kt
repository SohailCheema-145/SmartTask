package com.tcp.smarttasks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tcp.smarttasks.data.local.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}