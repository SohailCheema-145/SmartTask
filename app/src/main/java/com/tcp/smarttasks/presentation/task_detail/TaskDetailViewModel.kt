package com.tcp.smarttasks.presentation.task_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcp.smarttasks.domain.model.Task
import com.tcp.smarttasks.domain.usecase.CantResolveTaskUseCase
import com.tcp.smarttasks.domain.usecase.GetTaskUseCase
import com.tcp.smarttasks.domain.usecase.ResolveTaskUseCase
import com.tcp.smarttasks.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val resolveTaskUseCase: ResolveTaskUseCase,
    private val cantResolveTaskUseCase: CantResolveTaskUseCase
) : ViewModel() {

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    fun resolveTask(comment: String = "") {
        _selectedTask.value?.run {
            this.comment = comment
        }
        viewModelScope.launch {
            _selectedTask.value?.let { selectedTask ->
                resolveTaskUseCase(selectedTask).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _selectedTask.value = result.data
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    fun cantResolveTask(comment: String = "") {
        _selectedTask.value?.run {
            this.comment = comment.trim()
        }
        viewModelScope.launch {
            _selectedTask.value?.let { selectedTask ->
                cantResolveTaskUseCase(selectedTask).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _selectedTask.value = result.data
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    fun getTaskById(id: String) {
        viewModelScope.launch {
            // Collect only the first two emitted values
            getTaskUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (_selectedTask.value == null) {
                            _selectedTask.value = result.data
                        }
                    }

                    else -> {
                    }
                }
            }
        }
    }

}
