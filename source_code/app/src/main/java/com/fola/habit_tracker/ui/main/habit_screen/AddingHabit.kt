package com.fola.habit_tracker.ui.main.habit_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.habit_tracker.data.data_base.RepeatedType
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingHabitScreen(
    viewModel: AddingHabitsViewmodel = viewModel()
) {

    val habit = viewModel.habit.collectAsState()
    var expanded by remember { mutableStateOf(false) }


    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {

            OutlinedTextField(
                value = habit.value.title,
                onValueChange = { newTitle -> viewModel.setHabitTitle(newTitle) },
                placeholder = {
                    Text(
                        "Title",
                        fontFamily = interFont
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = habit.value.description,
                onValueChange = { newTitle -> viewModel.setHabitDescription(newTitle) },
                placeholder = {
                    Text(
                        "Description (optional)",
                        fontFamily = interFont
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default
            )

            CustomDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                ColorDropdown(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth(),
                    selectedColor = habit.value.color,
                    colorName = habit.value.colorName,
                    onColorSelected = { color, name -> viewModel.setHabitColor(color, name) },
                    colorOptions = habitColors
                )

                Box(modifier = Modifier.weight(.5f)) {

                    IconsDropMenu(
                        modifier = Modifier,
                        icon = habit.value.icon,
                        description = habit.value.iconDescription,
                        onSelectedIcon = { icon, des -> viewModel.setHabitIcon(icon, des) },
                        iconsList = habitIcons
                    )
                }


            }

            CustomDivider()

            RepeatRow(
                repeatedType = habit.value.repeatedType,
                onRepeatTypeChange = { r, l -> viewModel.setHabitRepeat(r, l) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            TimeSetting(
                onTimeChange = { viewModel.setHabitTime(it) },
                time = habit.value.startTime
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = "Start Date",
                onValueChange = {},
                readOnly = true,
                label = { Text("Start Date") },

                leadingIcon = {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = if (habit.value.endDate == LocalDate.MAX) "End Date (optional)" else habit.value.endDate.toString(),
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = "Daily goal",
                    onValueChange = {},
                    modifier = Modifier
                        .weight(.4f)
                        .fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "Unit per day",
                        onValueChange = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        readOnly = false,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(RepeatedType.ONCE.name) },
                            onClick = {
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(RepeatedType.DAILY.name) },
                            onClick = {
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(RepeatedType.WEEKLY.name) },
                            onClick = {
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(RepeatedType.MONTHLY.name) },
                            onClick = {

                            }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            // Bottom buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    TextButton(onClick = {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatRow(
    modifier: Modifier = Modifier,
    repeatedType: RepeatedType,
    onRepeatTypeChange: (RepeatedType, List<Int>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = repeatedType.name,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                placeholder = {
                    Text("Repeated Type")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                RepeatedType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            onRepeatTypeChange(type, emptyList())
                            expanded = false
                        }
                    )
                }

            }
        }
        when (repeatedType) {
            RepeatedType.ONCE -> {}
            RepeatedType.DAILY -> {}
            RepeatedType.WEEKLY -> {}
            RepeatedType.MONTHLY -> {}
            RepeatedType.YEARLY -> {}
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSetting(
    modifier: Modifier = Modifier,
    onTimeChange: (LocalTime) -> Unit,
    time: LocalTime,
) {
    var timeValue by remember { mutableStateOf(time.toString()) }
    var showDialog by remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute
    )

    Log.d("Clickeded", showDialog.toString())

    if (showDialog) {
        Log.d("Clickedddddd", showDialog.toString())
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
                    modifier = Modifier.fillMaxSize(),
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
                                timeValue = selectedTime.toString()
                                onTimeChange(selectedTime)
                                showDialog = false
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


    OutlinedTextField(
        value = timeValue,
        onValueChange = {}, // No-op since it's read-only
        readOnly = true,
        label = { Text("Start Time") },
        leadingIcon = {
            Icon(
                Icons.Default.AlarmAdd,
                contentDescription = "time",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Log.d("clicked", "inside textBox")
                showDialog = true
                Log.d("click", "inside text box $showDialog")
            }
    )
    Text(text = timeValue, Modifier.clickable {
        Log.d("clicked", "inside text")
        showDialog = true
        Log.d("click", "inside text $showDialog")
    })
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddingTaskLightPrev() {
    AppTheme {
        AddingHabitScreen(
            viewModel(
            )
        )
    }
}
//
//@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun AddingTaskDarkPrev() {
//    AppTheme {
//        AddingHabitScreen(
//            viewModel(
//            )
//        )
//    }
//}