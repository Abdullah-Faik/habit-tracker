package com.fola.habit_tracker.ui.auth.viewmodel

import com.fola.habit_tracker.ui.components.UiState

data class FieldHandler(
    val text: String = "",
    val state: UiState = UiState.IDLE,
    val errorMessage: String = ""
)


data class LoginScreenState(
    val email: FieldHandler = FieldHandler(),
    val password: FieldHandler = FieldHandler()
)


enum class AuthScreen() {
    LOGIN,
    REGISTER,
    RESET_PASSWORD,
    START

}