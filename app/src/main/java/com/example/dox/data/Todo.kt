package com.example.dox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    // Countdown feature fields
    val isCountdownType: Boolean = false,
    val totalCount: Int = 0, // Total amount to be done (e.g., 150 questions)
    val completedCount: Int = 0 // Amount completed so far (e.g., 10 questions)
) {
    // Helper properties for countdown todos
    val remainingCount: Int
        get() = if (isCountdownType) totalCount - completedCount else 0
    
    val progressPercentage: Float
        get() = if (isCountdownType && totalCount > 0) {
            (completedCount.toFloat() / totalCount.toFloat()) * 100f
        } else 0f
        
    val isCountdownCompleted: Boolean
        get() = if (isCountdownType) completedCount >= totalCount else isCompleted
}
