package com.fola.habit_tracker.screens.ui_screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R


enum class UiState {
    GOOD, ERROR, IDLE
}


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
            errorTextColor = colorResource(R.color.Red),
            cursorColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Email,
    uiState: UiState = UiState.IDLE,
    errorMessage: String = ""
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue) },
        placeholder = {
            Text(
                placeholder,
            )
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None
        else PasswordVisualTransformation(mask = '●'),
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
            errorTextColor = colorResource(R.color.Red),
            cursorColor = Color.Black
        ),
        trailingIcon = {
            val image = if (isPasswordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff
            val description = if (isPasswordVisible) "Hide password" else "Show password"
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = image,
                    description,
                    tint = colorResource(R.color.basic_background)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = ""
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.main_color)
        )
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
        )
    }


}