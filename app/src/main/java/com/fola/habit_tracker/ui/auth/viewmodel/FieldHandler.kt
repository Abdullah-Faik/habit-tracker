package com.fola.habit_tracker.ui.auth.viewmodel

import com.fola.habit_tracker.ui.components.UiState

data class FieldHandler(
    val value: String = "",
    val state: UiState = UiState.IDLE,
    val errorMessage: String = ""
)


data class LoginScreenState(
    val email: FieldHandler = FieldHandler(),
    val password: FieldHandler = FieldHandler()
)


data class AddNewAccount(
    val name: FieldHandler = FieldHandler(),
    val email: FieldHandler = FieldHandler(),
    val password: FieldHandler = FieldHandler(),
    val rePassword: FieldHandler = FieldHandler()
)


data class ResetPassword(
    val email: FieldHandler = FieldHandler()
)
