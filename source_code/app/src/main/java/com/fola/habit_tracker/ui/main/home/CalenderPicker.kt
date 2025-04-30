package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalenderPicker(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                   // onConfirm(LocalDate.ofEpochDay())
                }
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {onDismiss()}) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(datePickerState)
    }


}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalenderPickerPrevDark() {
    AppTheme {
        CalenderPicker(Modifier,{},{})
    }

}

@Preview(showBackground = false)
@Composable
private fun CalenderPickerPrev() {
    AppTheme {
        CalenderPicker(Modifier,{},{})
    }

}

