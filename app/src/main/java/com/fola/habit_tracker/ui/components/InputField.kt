package com.fola.habit_tracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fola.habit_tracker.R


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Email,
    uiState: UiState = UiState.IDLE,
    errorMessage: String = ""
) {
    TextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue) },
        placeholder = {
            Text(
                placeholder,
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        isError = uiState == UiState.ERROR,
        supportingText = {
            if (uiState == UiState.ERROR && errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            errorContainerColor = Color.White,
            errorTextColor = colorResource(R.color.Red),
            cursorColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}
