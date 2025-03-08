package com.fola.habit_tracker.ui.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.fola.habit_tracker.ui.components.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResetPasswordViewmodel : ViewModel() {


    private val _emailState = MutableStateFlow(FieldHandler())
    val emailState = _emailState.asStateFlow()


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


}