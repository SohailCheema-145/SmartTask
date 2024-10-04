package com.tcp.smarttasks.presentation.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.domain.usecase.GetAllTasksUseCase
import com.tcp.smarttasks.domain.usecase.GetTasksForSpecificDateUseCase
import com.tcp.smarttasks.utils.DateUtils
import com.tcp.smarttasks.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTasksForSpecificDateUseCase: GetTasksForSpecificDateUseCase,
) : ViewModel() {

    private val _tasks = MutableStateFlow<Resource<List<Task>>>(Resource.Loading())
    val tasks: StateFlow<Resource<List<Task>>> = _tasks
    private val _selectedDate = MutableStateFlow(DateUtils.getFormattedTodayDate())
    val selectedDate: StateFlow<String> = _selectedDate

    init {
        //fetch new tasks from api
        fetchAllTasks()
    }

    private fun fetchAllTasks() {
        viewModelScope.launch {
            getAllTasksUseCase().collect {
//                after api call show only today tasks
                fetchTasksForSpecificDate(selectedDate.value)
            }
        }
    }

    fun getNextDayTasks() {
        DateUtils.getNextDay(selectedDate.value)?.let {
            _selectedDate.value = it
            fetchTasksForSpecificDate(it)
        }
    }

    fun getPreviousDayTasks() {
        DateUtils.getPreviousDay(selectedDate.value)?.let {
            _selectedDate.value = it
            fetchTasksForSpecificDate(it)
        }
    }

    private fun fetchTasksForSpecificDate(date: String) {
        viewModelScope.launch {
            getTasksForSpecificDateUseCase(date).collect { result ->
                _tasks.value = result
            }
        }
    }
}
