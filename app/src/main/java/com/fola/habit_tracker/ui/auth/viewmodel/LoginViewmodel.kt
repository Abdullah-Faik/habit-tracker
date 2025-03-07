package com.fola.habit_tracker.ui.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.fola.habit_tracker.ui.components.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewmodel : ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()


    fun updateEmailField(text: String) {
        _loginScreenState.update { loginState ->
            loginState.copy(
                email = loginState.email.copy(
                    value = text
                )
            )
        }
        validateEmail()
    }


    private fun validateEmail(
        errorMessage: String = "Email is not valid"
    ): Boolean {
        var isValid = false
        if (_loginScreenState.value.email.value.isEmpty()) {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    email = FieldHandler()
                )
            }
        } else if (Patterns.EMAIL_ADDRESS.matcher(_loginScreenState.value.email.value).matches()) {
            isValid = true
            _loginScreenState.update { loginState ->
                loginState.copy(
                    email = _loginScreenState.value.email.copy(
                        state = UiState.GOOD,
                        errorMessage = ""

                    )
                )

            }
        } else {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    email = _loginScreenState.value.email.copy(
                        state = UiState.ERROR,
                        errorMessage = errorMessage
                    )
                )
            }
        }
        return isValid
    }

    fun updatePassword(text: String) {
        _loginScreenState.update { loginState ->
            loginState.copy(
                password = loginState.password.copy(
                    value = text
                )
            )
        }
        validatePassword()
    }

    private fun validatePassword(
        errorMessage: String = "Password is short"
    ): Boolean {
        var isValid = false
        if (_loginScreenState.value.password.value.isEmpty()) {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    password = FieldHandler()
                )
            }
        } else if (_loginScreenState.value.password.value.length >= 8) {
            isValid = true
            _loginScreenState.update { loginState ->
                loginState.copy(
                    password = _loginScreenState.value.password.copy(
                        state = UiState.GOOD,
                        errorMessage = ""

                    )
                )

            }
        } else {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    password = _loginScreenState.value.password.copy(
                        state = UiState.ERROR,
                        errorMessage = errorMessage
                    )
                )
            }
        }
        return isValid
    }


}