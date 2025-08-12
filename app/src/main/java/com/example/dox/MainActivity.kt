package com.example.dox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.dox.data.TodoDatabase
import com.example.dox.repository.TodoRepository
import com.example.dox.ui.screens.TodoScreen
import com.example.dox.ui.theme.DoXTheme
import com.example.dox.viewmodel.TodoViewModel
import com.example.dox.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    
    private val todoViewModel: TodoViewModel by viewModels {
        val database = TodoDatabase.getDatabase(this)
        val repository = TodoRepository(database.todoDao())
        TodoViewModelFactory(repository)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoXTheme {
                TodoScreen(
                    viewModel = todoViewModel
                )
            }
        }
    }
}