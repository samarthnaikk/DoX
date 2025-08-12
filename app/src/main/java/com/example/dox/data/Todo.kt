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
    val dueDate: Long? = null, // null means no due date set
    // Priority feature
    val priority: String? = null // null means no priority set, stores Priority enum name
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
    
    // Helper properties for priority
    val priorityEnum: Priority?
        get() = Priority.fromString(priority)
    
    // Helper properties for countdown pace tracking
    val daysSinceCreation: Int
        get() = if (isCountdownType) {
            val dayInMillis = 24 * 60 * 60 * 1000L
            val daysPassed = (System.currentTimeMillis() - createdAt) / dayInMillis
            daysPassed.toInt().coerceAtLeast(1) // At least 1 day to avoid division by zero
        } else 0
    
    val daysUntilDue: Int?
        get() = if (isCountdownType && dueDate != null) {
            val dayInMillis = 24 * 60 * 60 * 1000L
            val daysRemaining = (dueDate!! - System.currentTimeMillis()) / dayInMillis
            daysRemaining.toInt().coerceAtLeast(0)
        } else null
    
    val requiredDailyPace: Double
        get() = if (isCountdownType && daysUntilDue != null && daysUntilDue!! > 0) {
            remainingCount.toDouble() / daysUntilDue!!.toDouble()
        } else 0.0
    
    val currentDailyPace: Double
        get() = if (isCountdownType && daysSinceCreation > 0) {
            completedCount.toDouble() / daysSinceCreation.toDouble()
        } else 0.0
    
    val paceAnalysis: String
        get() = if (isCountdownType && dueDate != null && daysUntilDue != null) {
            val required = requiredDailyPace
            val current = currentDailyPace
            
            // Calculate where user should be vs where they actually are
            val shouldHaveCompleted = required * daysSinceCreation
            val actuallyCompleted = completedCount.toDouble()
            val itemsDifference = actuallyCompleted - shouldHaveCompleted
            
            when {
                isCountdownCompleted -> "✅ Task completed!"
                daysUntilDue!! <= 0 -> "⚠️ Due date passed"
                itemsDifference > 0.5 -> {
                    val daysAhead = if (required > 0) (itemsDifference / required).toInt() else 0
                    "🚀 ${String.format("%.1f", itemsDifference)} items ahead of schedule (${daysAhead} days ahead)"
                }
                itemsDifference < -0.5 -> {
                    val daysBehind = if (required > 0) (Math.abs(itemsDifference) / required).toInt() else 0
                    "⚠️ ${String.format("%.1f", Math.abs(itemsDifference))} items behind schedule (${daysBehind} days behind)"
                }
                else -> "✅ On track! Keep it up!"
            }
        } else ""
    
    val estimatedCompletionDate: String?
        get() = if (isCountdownType && currentDailyPace > 0 && !isCountdownCompleted) {
            val daysToComplete = remainingCount / currentDailyPace
            val completionTime = System.currentTimeMillis() + (daysToComplete * 24 * 60 * 60 * 1000).toLong()
            val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
            "At current pace: ${sdf.format(java.util.Date(completionTime))}"
        } else null
}
