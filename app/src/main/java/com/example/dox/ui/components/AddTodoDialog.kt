package com.example.dox.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddTodoDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddRegularTodo: (String, String) -> Unit,
    onAddCountdownTodo: (String, String, Int) -> Unit
) {
    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var isCountdownType by remember { mutableStateOf(false) }
        var totalCount by remember { mutableStateOf("") }
        
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
                                            onAddCountdownTodo(title, description, count)
                                            title = ""
                                            description = ""
                                            totalCount = ""
                                            onDismiss()
                                        }
                                    } else {
                                        onAddRegularTodo(title, description)
                                        title = ""
                                        description = ""
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
    }
}
