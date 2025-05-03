package com.fola.habit_tracker.ui.auth.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fola.habit_tracker.ui.components.UiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewmodel : ViewModel() {

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()
    private val _snackbarEvent = Channel<String>(Channel.BUFFERED)
    val snackbarEvent = _snackbarEvent.receiveAsFlow()



    fun updateEmailField(text: String) {
        _loginScreenState.update { loginState ->
            loginState.copy(
                email = loginState.email.copy(
                    text = text
                )
            )
        }
        validateEmail()
    }


    private fun validateEmail(
        errorMessage: String = "Email is not valid"
    ): Boolean {
        var isValid = false
        if (_loginScreenState.value.email.text.isEmpty()) {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    email = FieldHandler()
                )
            }
        } else if (Patterns.EMAIL_ADDRESS.matcher(_loginScreenState.value.email.text).matches()) {
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
                    text = text
                )
            )
        }
        validatePassword()
    }

    private fun validatePassword(
        errorMessage: String = "Password is short"
    ): Boolean {
        var isValid = false
        if (_loginScreenState.value.password.text.isEmpty()) {
            _loginScreenState.update { loginState ->
                loginState.copy(
                    password = FieldHandler()
                )
            }
        } else if (_loginScreenState.value.password.text.length >= 8) {
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

    private fun validateFields(): Boolean {
        var isValid = true

        if (_loginScreenState.value.email.state != UiState.GOOD) {
            isValid = false
            _loginScreenState.update { loginState ->
                loginState.copy(
                    email = _loginScreenState.value.email.copy(
                        state = UiState.ERROR,
                        errorMessage = _loginScreenState.value.email.errorMessage.ifEmpty {
                            "Email Required"
                        }
                    )
                )

            }
        }

        if (_loginScreenState.value.password.state != UiState.GOOD) {
            isValid = false
            _loginScreenState.update { loginState ->
                loginState.copy(
                    password = _loginScreenState.value.password.copy(
                        errorMessage = _loginScreenState.value.password.errorMessage.ifEmpty
                        { "Password Required" },
                        state = UiState.ERROR
                    )
                )
            }
        }
        return isValid

    }

    fun signIn(onSuccess: () -> Unit) {
        if (validateFields()) {
            val email = _loginScreenState.value.email.text
            val password = _loginScreenState.value.password.text
            val auth = Firebase.auth

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            showSnackbar("Login Successfully")
                            onSuccess()
                        } else {
                            showSnackbar("Please verify your email address.")
                        }
                    } else {
                        showSnackbar(task.exception?.message.toString())
                    }
                }
        }
    }


    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarEvent.send(message)
        }
    }


}