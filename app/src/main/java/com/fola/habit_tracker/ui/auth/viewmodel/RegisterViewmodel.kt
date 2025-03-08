package com.fola.habit_tracker.ui.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.fola.habit_tracker.ui.components.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewmodel : ViewModel() {

    private val _nameState = MutableStateFlow(FieldHandler())
    private val _emailState = MutableStateFlow(FieldHandler())
    private val _password = MutableStateFlow(FieldHandler())
    private val _rePassword = MutableStateFlow(FieldHandler())

    val nameState = _nameState.asStateFlow()
    val emailState = _emailState.asStateFlow()
    val password = _password.asStateFlow()
    val rePassword = _rePassword.asStateFlow()


    fun updateEmail(text: String) {
        _emailState.update {
            it.copy(
                text = text
            )
        }
        validateEmail()
    }

    private fun validateEmail(): Boolean {
        var isValid = false
        if (_emailState.value.text.isEmpty()) {
            _emailState.value = FieldHandler()
        } else if (Patterns.EMAIL_ADDRESS.matcher(_emailState.value.text).matches()) {
            isValid = true
            _emailState.update {
                it.copy(
                    state = UiState.GOOD,
                    errorMessage = ""
                )
            }
        } else {
            _emailState.update {
                it.copy(
                    state = UiState.ERROR,
                    errorMessage = "Email is not valid"
                )
            }

        }
        return isValid
    }

    fun updateName(text: String) {
        _nameState.update { nameState ->
            nameState.copy(
                text = text
            )
        }
        validateName()
    }

    private fun validateName(error: String = ""): Boolean {
        var isValid = false
        if (_nameState.value.text.isEmpty()) {
            if (error.isEmpty())
                _nameState.value = FieldHandler()
            else {
                _nameState.update {
                    it.copy(
                        state = UiState.ERROR,
                        errorMessage = error
                    )
                }
            }
        } else {
            isValid = true
            _nameState.update {
                it.copy(
                    state = UiState.GOOD,
                    errorMessage = ""
                )
            }

        }
        return isValid
    }


    fun updatePassword(text: String) {
        _password.update {
            it.copy(
                text = text
            )
        }
        validatePassword("password is short")
        validateRePassword("password is not match")
    }

    private fun validatePassword(error: String): Boolean {
        var isValid = false
        if (_password.value.text.isEmpty()) {
            _password.value = FieldHandler()
        } else if (_password.value.text.length >= 8) {
            isValid = true
            _password.update {
                it.copy(
                    state = UiState.GOOD,
                    errorMessage = error
                )
            }
        } else {
            _password.update {
                it.copy(
                    state = UiState.ERROR,
                    errorMessage = error
                )
            }
        }
        return isValid
    }


    fun updateRePassword(text: String) {
        _rePassword.update {
            it.copy(
                text = text
            )
        }
        validateRePassword("password is not match")
    }

    private fun validateRePassword(error: String): Boolean {
        var isValid = false
        if (_rePassword.value.text.isEmpty()) {
            _rePassword.value = FieldHandler()
        } else if (_rePassword.value.text == _password.value.text) {
            isValid = true
            _rePassword.update {
                it.copy(
                    state = UiState.GOOD,
                    errorMessage = ""
                )
            }
        } else {
            _rePassword.update {
                it.copy(
                    state = UiState.ERROR,
                    errorMessage = error
                )
            }
        }
        return isValid
    }


}