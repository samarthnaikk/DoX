package com.example.dox.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dox.data.Priority
import com.example.dox.data.Todo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoDialog(
    showDialog: Boolean,
    todo: Todo?,
    onDismiss: () -> Unit,
    onUpdateTodo: (Todo) -> Unit
) {
    if (showDialog && todo != null) {
        var title by remember { mutableStateOf(todo.title) }
        var description by remember { mutableStateOf(todo.description) }
        var selectedDate by remember { mutableStateOf(todo.dueDate) }
        var selectedPriority by remember { mutableStateOf(todo.priorityEnum) }
        var showDatePicker by remember { mutableStateOf(false) }
        var showPriorityDropdown by remember { mutableStateOf(false) }
        
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = todo.dueDate
        )
        
        LaunchedEffect(todo) {
            title = todo.title
            description = todo.description
            selectedDate = todo.dueDate
            selectedPriority = todo.priorityEnum
        }
        
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Edit Todo",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title*") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    
                    // Show total count for countdown tasks (read-only)
                    if (todo.isCountdownType) {
                        OutlinedTextField(
                            value = todo.totalCount.toString(),
                            onValueChange = { },
                            label = { Text("Total Count (Cannot be changed)") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            enabled = false
                        )
                    }
                    
                    // Priority Selection
                    ExposedDropdownMenuBox(
                        expanded = showPriorityDropdown,
                        onExpandedChange = { showPriorityDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = selectedPriority?.displayName ?: "No Priority",
                            onValueChange = { },
                            label = { Text("Priority (Optional)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriorityDropdown)
                            }
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showPriorityDropdown,
                            onDismissRequest = { showPriorityDropdown = false }
                        ) {
                            DropdownMenuItem(
                                text = { 
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.outline,
                                                    CircleShape
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("No Priority")
                                    }
                                },
                                onClick = {
                                    selectedPriority = null
                                    showPriorityDropdown = false
                                }
                            )
                            
                            Priority.values().forEach { priority ->
                                DropdownMenuItem(
                                    text = { 
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(12.dp)
                                                    .background(
                                                        when (priority) {
                                                            Priority.HIGH -> Color(0xFFE53E3E)
                                                            Priority.MEDIUM -> Color(0xFFFF9500)
                                                            Priority.LOW -> Color(0xFF38A169)
                                                        },
                                                        CircleShape
                                                    )
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(priority.displayName)
                                        }
                                    },
                                    onClick = {
                                        selectedPriority = priority
                                        showPriorityDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Due Date Selection
                    OutlinedTextField(
                        value = selectedDate?.let { formatDate(it) } ?: "",
                        onValueChange = { },
                        label = { Text("Due Date (Optional)") },
                        placeholder = { Text("Select due date") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            Row {
                                if (selectedDate != null) {
                                    IconButton(onClick = { selectedDate = null }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Clear date",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Select date"
                                    )
                                }
                            }
                        }
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    val updatedTodo = todo.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        dueDate = selectedDate,
                                        priority = selectedPriority?.name
                                    )
                                    onUpdateTodo(updatedTodo)
                                    onDismiss()
                                }
                            },
                            enabled = title.isNotBlank()
                        ) {
                            Text("Update")
                        }
                    }
                }
            }
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { date ->
                    selectedDate = date
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
