package com.fola.habit_tracker.ui.main.habit_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateEditing(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    label: String,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    var value by remember { mutableStateOf(label) }


    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val lDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateChange(lDate)
                        value = lDate.toString()
                    }
                    showDialog = false

                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }



    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(8))
            .height(56.dp)
            .clickable(enabled = enable) {
                showDialog = true
            }
            .background(color = (if (!enable) Color(0x44444444) else Color.Transparent))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.CalendarMonth,
            contentDescription = "Date",
            tint = (if (!enable) Color(0x88888888) else MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxHeight()
        )

        Spacer(Modifier.width(16.dp))
        Text(text = value)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSetting(
    modifier: Modifier = Modifier,
    onTimeChange: (LocalTime) -> Unit,
    time: LocalTime,
) {
    var timeValue by remember { mutableStateOf("Notification Time (default 08:00 am)") }
    var showDialog by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val timeState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Column(
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimeInput(
                        state = timeState,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { showDialog = false },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = {
                                val selectedTime = LocalTime.of(timeState.hour, timeState.minute)
                                showDialog = false
                                onTimeChange(selectedTime)
                                timeValue = time.format(formatter)
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(8))
            .height(56.dp)
            .clickable {
                showDialog = true
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            Icons.Default.AlarmAdd,
            contentDescription = "time",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxHeight()
        )

        Spacer(Modifier.width(16.dp))

        Text(text = timeValue, Modifier.clickable {
            showDialog = true
        })
    }
}
