package com.fola.habit_tracker.ui.auth.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fola.habit_tracker.ui.components.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewmodel : ViewModel() {

    private val _nameState = MutableStateFlow(FieldHandler())
    private val _emailState = MutableStateFlow(FieldHandler())
    private val _password = MutableStateFlow(FieldHandler())
    private val _rePassword = MutableStateFlow(FieldHandler())
    private val _snackbarEvent = Channel<String>(Channel.BUFFERED)


    val nameState = _nameState.asStateFlow()
    val emailState = _emailState.asStateFlow()
    val password = _password.asStateFlow()
    val rePassword = _rePassword.asStateFlow()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()


    fun updateEmail(text: String) {
        _emailState.update {
            it.copy(
                text = text
            )
        }
        validateEmail()
    }

    private fun validateEmail(errorMsg: String = "Email is not valid"): Boolean {
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
                    errorMessage = errorMsg
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

    private fun validateName(errorMsg: String = ""): Boolean {
        var isValid = false
        if (_nameState.value.text.isEmpty()) {
            if (errorMsg.isEmpty())
                _nameState.value = FieldHandler()
            else {
                _nameState.update {
                    it.copy(
                        state = UiState.ERROR,
                        errorMessage = errorMsg
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

    private fun validatePassword(errorMsg: String): Boolean {
        var isValid = false
        if (_password.value.text.isEmpty()) {
            _password.value = FieldHandler()
        } else if (_password.value.text.length >= 8) {
            isValid = true
            _password.update {
                it.copy(
                    state = UiState.GOOD,
                    errorMessage = ""
                )
            }
        } else {
            _password.update {
                it.copy(
                    state = UiState.ERROR,
                    errorMessage = errorMsg
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

    private fun validateRePassword(errorMsg: String): Boolean {
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
                    errorMessage = errorMsg
                )
            }
        }
        return isValid
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (_emailState.value.state != UiState.GOOD) {
            isValid = false
            _emailState.update {
                _emailState.value.copy(
                    errorMessage = "Email Required",
                    state = UiState.ERROR
                )
            }
        }
        if (_nameState.value.state != UiState.GOOD) {
            isValid = false
            _nameState.update {
                _nameState.value.copy(
                    errorMessage = "Name Required",
                    state = UiState.ERROR
                )
            }
        }
        if (_password.value.state != UiState.GOOD) {
            isValid = false
            _password.update {
                _password.value.copy(
                    errorMessage = _password.value.errorMessage.ifEmpty { "Password Required" },
                    state = UiState.ERROR
                )
            }
        }
        if (_rePassword.value.state != UiState.GOOD) {
            isValid = false
            _rePassword.update {
                _rePassword.value.copy(
                    state = UiState.ERROR,
                    errorMessage = _rePassword.value.errorMessage.ifEmpty { "Confirmation Required" }
                )
            }
        }
        return isValid

    }


    fun addNewUser() {
        if (validateFields()) {
            val auth = Firebase.auth
            val name = _nameState.value.text
            val email = _emailState.value.text
            val password = _password.value.text
            auth.createUserWithEmailAndPassword(
                email, password
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("signup", "userAddedSuccess")
                    auth.currentUser?.let { user ->
                        verifiedEmail(user)
                        addUserToDatabase(
                            user,
                            name = name,
                            email = email
                        )
                    }
                } else {
                    Log.d("signup", "user can't be add")
                    Log.d("signup", task.exception?.localizedMessage ?: "error")
                    showSnackbar(task.exception?.message.toString())

                }
            }
        }
    }

    private fun verifiedEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("SNACKBAR", "MESSAGE IS $snackbarEvent")
                showSnackbar("Verification Email send check your mailbox!")
                Log.d("signup", "verification email send")
            } else {
                Log.d("signup", "can't send email")
                showSnackbar(it.exception?.message.toString())
            }
        }
    }

    private fun addUserToDatabase(user: FirebaseUser, email: String, name: String) {
        val userId = user.uid


        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "User profile updated: $name")
                } else {
                    Log.e("FirebaseAuth", "Profile update failed", task.exception)
                }
            }

        val userData = hashMapOf(
            "uuid" to userId,
            "name" to name,
            "email" to email
        )
        val db = Firebase.firestore
        db.collection("users")
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("add user", "document id is $userId")
            }.addOnFailureListener {
                Log.d("add user", it.toString())
            }

    }

    private fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarEvent.send(message)
        }
    }

}