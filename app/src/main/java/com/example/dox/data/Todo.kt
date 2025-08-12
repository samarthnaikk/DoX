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
    val completedCount: Int = 0, // Amount completed so far (e.g., 10 questions)
    // Due date feature
    val dueDate: Long? = null // null means no due date set
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
    
    // Helper properties for due date
    val isOverdue: Boolean
        get() = dueDate != null && dueDate < System.currentTimeMillis() && !isCompleted && (!isCountdownType || !isCountdownCompleted)
    
    val isDueSoon: Boolean
        get() = dueDate != null && !isOverdue && dueDate < System.currentTimeMillis() + (24 * 60 * 60 * 1000) // Due within 24 hours
}
