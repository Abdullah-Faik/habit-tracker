package com.fola.habit_tracker.ui.main.habit_screen

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddingHabitScreen(
    viewModel: AddingHabitsViewmodel = viewModel(
        factory = provideAddingHabitsViewModelFactory(LocalContext.current.applicationContext as Application)
    ),
    editHabit: Habit? = null
) {
    val habit = viewModel.habit.collectAsState()
    val isTitleError = viewModel.isTitleError.collectAsState()
    val isQuantityError = viewModel.isQuantityError.collectAsState()
    val isUnitError = viewModel.isUnitError.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    if (editHabit != null) {
        viewModel.setWholeHabit(editHabit)
    }

    Scaffold(modifier = Modifier.fillMaxWidth(),
        bottomBar = {}) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    brush = SolidColor(
                        Color(habit.value.color)
                    ),
                    alpha = 0.08f
                )
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = habit.value.title,
                onValueChange = { newTitle -> viewModel.setHabitTitle(newTitle) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default,
                isError = isTitleError.value,
                label = { if (isTitleError.value) Text("Required") else Text("Title") }

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
                    onColorSelected = { color, name ->
                        viewModel.setHabitColor(
                            color,
                            name
                        )
                    },
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
            Spacer(Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(25))
                    .clip(RoundedCornerShape(25))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    "Notification"
                )
                Switch(
                    checked = habit.value.notification == 1,
                    onCheckedChange = { viewModel.setNotification() }
                )
            }

            CustomDivider()

            RepeatRow(
                repeatedType = habit.value.repeatedType,
                onRepeatTypeChange = { r, l -> viewModel.setHabitRepeat(r, l) },
                habit = habit,
                viewmodel = viewModel
            )
            Spacer(modifier = Modifier.height(8.dp))

            TimeSetting(
                onTimeChange = { viewModel.setHabitTime(it) },
                time = habit.value.startTime
            )
            Spacer(modifier = Modifier.height(8.dp))

            DateEditing(

                enable = habit.value.repeatedType != RepeatedType.ONCE,
                date = habit.value.startDate,
                label = "Start Date (today default)",
                onDateChange = { viewModel.setStartDate(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            DateEditing(
                enable = habit.value.repeatedType != RepeatedType.ONCE,
                date = habit.value.endDate,
                label = "End Date (optional)",
                onDateChange = { viewModel.setEndDate(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = habit.value.timesOfUnit.takeIf { it > 0 }?.toString() ?: "",
                    onValueChange = { input ->
                        if (input.isEmpty()) {
                            viewModel.setHabitTimesUnit(0)
                        } else if (input.isDigitsOnly()) {
                            val num = input.toIntOrNull()
                            if (num != null && num > 0) {
                                viewModel.setHabitTimesUnit(num)
                            }
                        }
                    },
                    label = { if (isQuantityError.value) Text("Required") else Text("Quantity") },
                    isError = isQuantityError.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(0.4f)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = TextFieldValue(
                            habit.value.unit,
                            selection = TextRange(habit.value.unit.length)
                        ),
                        onValueChange = {
                            viewModel.setUnit(it.text)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        singleLine = true,
                        label = { if (isUnitError.value) Text("Required") else Text("Habit Unit") },
                        isError = isUnitError.value
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        unitsList.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    expanded = false
                                    viewModel.setUnit(unit)
                                }
                            )
                        }

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
                        viewModel.confirmationButton()
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

val unitsList = listOf(
    "minute",
    "page",
    "calories",
    "hours",
)

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