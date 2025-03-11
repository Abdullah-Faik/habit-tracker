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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewmodel : ViewModel() {


    private val _emailState = MutableStateFlow(FieldHandler())
    val emailState = _emailState.asStateFlow()
    private val _snackbarEvent = Channel<String>(Channel.BUFFERED)
    val snackbarEvent = _snackbarEvent.receiveAsFlow()


    fun updateEmail(text: String) {
        _emailState.update {
            it.copy(
                text = text
            )
        }
        validateEmail()
    }


    private fun validateEmail(error : String = "Email is not valid"): Boolean {
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
                    errorMessage =  error
                )
            }

        }
        return isValid
    }


    fun resetPassword() {
        if (validateEmail("Email is Required")){
            Firebase.auth.sendPasswordResetEmail(_emailState.value.text)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        Log.d("passwordReset","email send success")
                        showSnackbar("Reset Email send, Check your mailbox!")
                    } else {
                        Log.d("passwordReset","can't send email")
                        Log.d("passwordReset",it.exception?.localizedMessage ?: "error" )
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