package com.fola.habit_tracker.ui.main.habit_screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.task_screen.TaskNavigation
import com.fola.habit_tracker.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingHabitScreen(navController: NavController) {
    var taskName by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Task input
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                placeholder = {
                    Text(
                        "New Task",
                        color = Color(0xff353434),
                        fontFamily = interFont
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default
            )

            Spacer(modifier = Modifier.height(16.dp))
            Box {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = Color(0xff333232),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Date row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Date", modifier = Modifier.weight(1f), fontFamily = interFont)
                Button(
                    onClick = { /* TODO: Date Picker */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Today",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = interFont,
                    )
                }
            }
            Box {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = Color(0xff333232),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Time & Reminder row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AlarmAdd,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Time & Reminder", modifier = Modifier.weight(1f), fontFamily = interFont)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color = MaterialTheme.colorScheme.onPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "0",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontFamily = interFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Note section
            Box {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = Color(0xff333232),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.ChatBubble,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Note", fontFamily = interFont)
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Text(
                            "Add a note",
                            color = Color.Gray,
                            fontFamily = interFont
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Cyan
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // Bottom buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    TextButton(onClick = {
                        navController.popBackStack() // Pop back to TaskScreen
                    }) {
                        Text(
                            "Cancel",
                            color = Color.LightGray,
                            fontFamily = interFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    TextButton(onClick = {
                        // TODO: Save task logic here
                        navController.popBackStack() // Pop back to TaskScreen
                    }) {
                        Text(
                            "Confirm",
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = interFont,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddingTaskLightPrev() {
    AppTheme {
        AddingHabitScreen(navController = rememberNavController()) // CHANGED: Use TaskNavigation to ensure NavController is linked to NavHost
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddingTaskDarkPrev() {
    AppTheme {
        AddingHabitScreen(navController = rememberNavController()) // CHANGED: Use TaskNavigation to ensure NavController is linked to NavHost
    }
}