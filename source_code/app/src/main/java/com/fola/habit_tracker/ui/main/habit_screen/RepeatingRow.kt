package com.fola.habit_tracker.ui.main.habit_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatRow(
    modifier: Modifier = Modifier,
    repeatedType: RepeatedType,
    onRepeatTypeChange: (RepeatedType, MutableSet<Int>) -> Unit,
    habit: State<Habit>,
    viewmodel: AddingHabitsViewmodel
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
                            onRepeatTypeChange(type, mutableSetOf())
                            expanded = false
                        }
                    )
                }

            }
        }
        when (repeatedType) {
            RepeatedType.ONCE -> {
                DateEditing(
                    modifier = Modifier.weight(0.6f),
                    date = habit.value.startDate,
                    onDateChange = { viewmodel.setStartDate(it) },
                    label = "Date"
                )
            }

            RepeatedType.DAILY -> {}
            RepeatedType.WEEKLY -> {
                WeekDaysPicker(
                    modifier = Modifier.weight(.6f),
                    activeDays = habit.value.days,
                    onDayChange = {
                        viewmodel.setDay(it)
                        Log.d("inside ", habit.value.days.toString())
                    }
                )
            }
            RepeatedType.MONTHLY -> {}
            RepeatedType.YEARLY -> {
                DateEditing(
                    modifier = Modifier.weight(0.6f),
                    date = habit.value.startDate,
                    onDateChange = { viewmodel.setStartDate(it) },
                    label = "Date"
                )
            }
        }
    }

}


@Composable
fun WeekDaysPicker(
    modifier: Modifier = Modifier,
    activeDays: MutableSet<Int>,
    onDayChange: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(8))
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        java.time.DayOfWeek.entries.forEach { day ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        onDayChange(day.value)
                    }
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8))
                    .background(
                        if (activeDays.contains(day.value)) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center,

                ) {
                Text(
                    text = day.value.toString(),
                    modifier = Modifier
                        .padding(
                            8.dp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
            if (day.value < (7)) {
                VerticalDivider(
                    modifier = Modifier
                        .height(56.dp),
                    thickness = 1.dp
                )
            }
        }


    }


}


@Preview(showBackground = true)
@Composable
private fun Prev() {
    AppTheme {
        WeekDaysPicker(
            activeDays = mutableSetOf(1, 2, 5, 8),
            onDayChange = {}
        )
    }
}