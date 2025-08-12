package com.example.dox.repository

import com.example.dox.data.Todo
import com.example.dox.data.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val todoDao: TodoDao) {
    
    fun getAllTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos()
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)
    }

    suspend fun insertTodo(todo: Todo) {
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun deleteTodoById(id: Int) {
        todoDao.deleteTodoById(id)
    }

    suspend fun toggleTodoComplete(todo: Todo) {
        todoDao.updateTodo(todo.copy(isCompleted = !todo.isCompleted))
    }
    
    // Countdown specific methods
    suspend fun incrementCountdownProgress(todo: Todo, incrementBy: Int = 1) {
        if (todo.isCountdownType) {
            val newCompletedCount = (todo.completedCount + incrementBy).coerceAtMost(todo.totalCount)
            todoDao.updateTodo(todo.copy(completedCount = newCompletedCount))
        }
    }
    
    suspend fun decrementCountdownProgress(todo: Todo, decrementBy: Int = 1) {
        if (todo.isCountdownType) {
            val newCompletedCount = (todo.completedCount - decrementBy).coerceAtLeast(0)
            todoDao.updateTodo(todo.copy(completedCount = newCompletedCount))
        }
    }
    
    suspend fun updateCountdownProgress(todo: Todo, newCompletedCount: Int) {
        if (todo.isCountdownType) {
            val validCount = newCompletedCount.coerceIn(0, todo.totalCount)
            todoDao.updateTodo(todo.copy(completedCount = validCount))
        }
    }
}
