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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddRegularTodo: (String, String, Long?, Priority?) -> Unit,
    onAddCountdownTodo: (String, String, Int, Long?, Priority?) -> Unit
) {
    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var isCountdownType by remember { mutableStateOf(false) }
        var totalCount by remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<Long?>(null) }
        var selectedPriority by remember { mutableStateOf<Priority?>(null) }
        var showPriorityDropdown by remember { mutableStateOf(false) }
        
        val datePickerState = rememberDatePickerState()
        
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
                        text = "Add New Todo",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    // Todo Type Selection
                    Column {
                        Text(
                            text = "Todo Type",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = !isCountdownType,
                                    onClick = { isCountdownType = false },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !isCountdownType,
                                onClick = { isCountdownType = false }
                            )
                            Text(
                                text = "Regular Task",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isCountdownType,
                                    onClick = { isCountdownType = true },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isCountdownType,
                                onClick = { isCountdownType = true }
                            )
                            Text(
                                text = "Countdown Task",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    
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
                    
                    // Countdown specific field
                    if (isCountdownType) {
                        OutlinedTextField(
                            value = totalCount,
                            onValueChange = { totalCount = it },
                            label = { Text("Total Count*") },
                            placeholder = { Text("e.g., 150") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            }
                        }
                    )
                    
                    // Clear due date button if date is selected
                    if (selectedDate != null) {
                        TextButton(
                            onClick = { selectedDate = null },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Clear Due Date")
                        }
                    }
                    
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
                                    if (isCountdownType) {
                                        val count = totalCount.toIntOrNull()
                                        if (count != null && count > 0) {
                                            onAddCountdownTodo(title, description, count, selectedDate, selectedPriority)
                                            title = ""
                                            description = ""
                                            totalCount = ""
                                            selectedDate = null
                                            selectedPriority = null
                                            onDismiss()
                                        }
                                    } else {
                                        onAddRegularTodo(title, description, selectedDate, selectedPriority)
                                        title = ""
                                        description = ""
                                        selectedDate = null
                                        selectedPriority = null
                                        onDismiss()
                                    }
                                }
                            },
                            enabled = title.isNotBlank() && (!isCountdownType || (totalCount.toIntOrNull() ?: 0) > 0)
                        ) {
                            Text("Add")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
