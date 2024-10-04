package com.tcp.smarttasks.di

import android.app.Application
import androidx.room.Room
import com.tcp.smarttasks.data.local.TaskDatabase
import com.tcp.smarttasks.data.remote.ApiService
import com.tcp.smarttasks.data.repository.TaskRepositoryImpl
import com.tcp.smarttasks.domain.repository.TaskRepository
import com.tcp.smarttasks.domain.usecase.GetAllTasksUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "tasks_db").build()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://demo1414406.mockable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskRepository(apiService: ApiService, db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(apiService, db.taskDao())
    }

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: TaskRepository): GetAllTasksUseCase {
        return GetAllTasksUseCase(repository)
    }
}
