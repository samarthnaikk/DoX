package com.example.dox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.dox.data.Todo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoItem(
    todo: Todo,
    onToggleComplete: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    onIncrementCount: ((Todo) -> Unit)? = null,
    onDecrementCount: ((Todo) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                todo.isOverdue -> MaterialTheme.colorScheme.errorContainer
                todo.isDueSoon -> MaterialTheme.colorScheme.tertiaryContainer
                todo.isCountdownType && todo.isCountdownCompleted -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox for regular todos or completion indicator for countdown
                if (!todo.isCountdownType) {
                    Checkbox(
                        checked = todo.isCompleted,
                        onCheckedChange = { onToggleComplete(todo) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                
                // Todo content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = todo.title,
                            style = MaterialTheme.typography.titleMedium,
                            textDecoration = if (!todo.isCountdownType && todo.isCompleted) TextDecoration.LineThrough else null,
                            color = if (!todo.isCountdownType && todo.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) 
                                   else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Countdown type indicator
                        if (todo.isCountdownType) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "COUNTDOWN",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    
                    if (todo.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = todo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatDate(todo.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    
                    // Due date information
                    todo.dueDate?.let { dueDate ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (todo.isOverdue) Icons.Default.Warning else Icons.Default.DateRange,
                                contentDescription = if (todo.isOverdue) "Overdue" else "Due date",
                                modifier = Modifier.size(14.dp),
                                tint = when {
                                    todo.isOverdue -> MaterialTheme.colorScheme.error
                                    todo.isDueSoon -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when {
                                    todo.isOverdue -> "Overdue: ${formatDate(dueDate)}"
                                    todo.isDueSoon -> "Due soon: ${formatDate(dueDate)}"
                                    else -> "Due: ${formatDate(dueDate)}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    todo.isOverdue -> MaterialTheme.colorScheme.error
                                    todo.isDueSoon -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                },
                                fontWeight = if (todo.isOverdue) FontWeight.Medium else FontWeight.Normal
                            )
                        }
                    }
                }
                
                // Delete button
                IconButton(
                    onClick = { onDelete(todo) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete todo",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            // Countdown progress section
            if (todo.isCountdownType) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Progress bar
                LinearProgressIndicator(
                    progress = { todo.progressPercentage / 100f },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (todo.isCountdownCompleted) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.secondary,
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Progress text
                    Column {
                        Text(
                            text = "${todo.completedCount} / ${todo.totalCount}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${String.format("%.1f", todo.progressPercentage)}% complete • ${todo.remainingCount} remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    // +/- buttons
                    Row {
                        IconButton(
                            onClick = { onDecrementCount?.invoke(todo) },
                            enabled = todo.completedCount > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Decrease count",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        IconButton(
                            onClick = { onIncrementCount?.invoke(todo) },
                            enabled = todo.completedCount < todo.totalCount
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase count",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
