package com.example.dox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dox.data.Todo
import com.example.dox.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllTodos().collect { todoList ->
                _todos.value = todoList
                _isLoading.value = false
            }
        }
    }

    fun addTodo(title: String, description: String = "") {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            val todo = Todo(
                title = title.trim(),
                description = description.trim()
            )
            repository.insertTodo(todo)
        }
    }
    
    fun addCountdownTodo(title: String, description: String = "", totalCount: Int) {
        if (title.isBlank() || totalCount <= 0) return
        
        viewModelScope.launch {
            val todo = Todo(
                title = title.trim(),
                description = description.trim(),
                isCountdownType = true,
                totalCount = totalCount,
                completedCount = 0
            )
            repository.insertTodo(todo)
        }
    }

    fun toggleTodoComplete(todo: Todo) {
        viewModelScope.launch {
            repository.toggleTodoComplete(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }
    
    // Countdown specific methods
    fun incrementCountdownProgress(todo: Todo, incrementBy: Int = 1) {
        viewModelScope.launch {
            repository.incrementCountdownProgress(todo, incrementBy)
        }
    }
    
    fun decrementCountdownProgress(todo: Todo, decrementBy: Int = 1) {
        viewModelScope.launch {
            repository.decrementCountdownProgress(todo, decrementBy)
        }
    }
    
    fun updateCountdownProgress(todo: Todo, newCompletedCount: Int) {
        viewModelScope.launch {
            repository.updateCountdownProgress(todo, newCompletedCount)
        }
    }
}
