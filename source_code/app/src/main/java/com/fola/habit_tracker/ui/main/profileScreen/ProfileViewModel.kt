package com.fola.habit_tracker.ui.main.profileScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

open class ProfileViewModel(
    private val localRepo: LocalProfileRepository,
    private val remoteRepo: RemoteProfileRepository
) : ViewModel() {
    private val TAG = "ProfileViewModel"

    open val userProfile: StateFlow<UserProfile> = localRepo.userProfile
    open val isNotificationsEnabled: StateFlow<Boolean> = localRepo.isNotificationsEnabled
    open val isDarkTheme: StateFlow<Boolean> = localRepo.isDarkTheme

    init {
        Log.d(TAG, "ProfileViewModel initialized")
        loadUserProfileFromFirebase()
    }

    private fun getApplicationContext(): Context {
        return try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as Context
        } catch (e: Exception) {
            throw IllegalStateException("Cannot access application context", e)
        }
    }

    fun loadUserProfileFromFirebase() {
        Log.d(TAG, "loadUserProfileFromFirebase")
        viewModelScope.launch {
            remoteRepo.loadUserProfile(
                onSuccess = { remoteProfile ->
                    Log.i(TAG, "loadUserProfileFromFirebase: success, remoteProfileImageUri=${remoteProfile.profileImageUri}")
                    localRepo.updateName(remoteProfile.name)
                    localRepo.updateEmail(remoteProfile.email)
                    localRepo.updatePassword(remoteProfile.Password)
                    // Only update profileImageUri if remote value is non-empty
                    if (remoteProfile.profileImageUri.isNotEmpty()) {
                        localRepo.updateProfileImage(remoteProfile.profileImageUri)
                    }
                    localRepo.toggleNotifications(remoteProfile.notificationsEnabled)
                    if (remoteProfile.darkTheme != isDarkTheme.value) {
                        localRepo.toggleTheme()
                    }
                    Log.i(TAG, "loadUserProfileFromFirebase: localProfileImageUri=${localRepo.getCurrentProfile().profileImageUri}")
                },
                onError = { e ->
                    Log.e(TAG, "loadUserProfileFromFirebase: error=${e.message}")
                }
            )
        }
    }

    fun onNameChanged(newName: String) {
        Log.d(TAG, "onNameChanged: name=$newName")
        localRepo.updateName(newName)
        syncProfileToFirebase()
        Log.i(TAG, "onNameChanged: success")
    }

    fun updateEmail(
        newEmail: String,
        currentPassword: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "updateEmail: newEmail=$newEmail")
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    Log.e(TAG, "updateEmail: No user logged in")
                    onError("No user logged in")
                    return@launch
                }

                if (!user.isEmailVerified) {
                    Log.e(TAG, "updateEmail: Current email not verified")
                    onError("Please verify your current email before updating it.")
                    return@launch
                }

                val currentEmail = user.email ?: throw IllegalStateException("User email is null")
                val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)
                user.reauthenticate(credential).await()
                Log.i(TAG, "updateEmail: Re-authentication successful")

                val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl("https://habit-tracker-7-3-2025.web.app/__/auth/action")
                    .setHandleCodeInApp(true)
                    .setAndroidPackageName("com.fola.habit_tracker", true, "1.0")
                    .build()

                FirebaseAuth.getInstance().sendSignInLinkToEmail(newEmail, actionCodeSettings)
                    .addOnSuccessListener {
                        Log.i(TAG, "updateEmail: Verification email sent to $newEmail")
                        Toast.makeText(
                            context,
                            "A verification email has been sent to $newEmail. Please verify it before updating.",
                            Toast.LENGTH_LONG
                        ).show()
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e(
                            TAG,
                            "updateEmail: Failed to send verification email, error=${e.message}"
                        )
                        onError("Failed to send verification email: ${e.message}")
                    }
            } catch (e: Exception) {
                Log.e(TAG, "updateEmail: error=${e.message}, stacktrace=${e.stackTraceToString()}")
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                    is FirebaseAuthException -> when (e.errorCode) {
                        "ERROR_INVALID_EMAIL" -> "Invalid email format."
                        else -> "Failed to process email update: ${e.message}"
                    }
                    else -> "Failed to process email update: ${e.message}"
                }
                onError(errorMessage)
            }
        }
    }

    fun confirmEmailUpdate(newEmail: String, currentPassword: String, context: Context) {
        Log.d(TAG, "confirmEmailUpdate: newEmail=$newEmail")
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    Log.e(TAG, "confirmEmailUpdate: No user logged in")
                    Toast.makeText(context, "No user logged in", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val currentEmail = user.email ?: throw IllegalStateException("User email is null")
                val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)
                user.reauthenticate(credential).await()
                Log.i(TAG, "confirmEmailUpdate: Re-authentication successful")

                user.reload().await()

                @Suppress("DEPRECATION")
                user.updateEmail(newEmail).await()
                Log.i(TAG, "confirmEmailUpdate: Email updated successfully in Firebase Auth")

                localRepo.updateEmail(newEmail)

                val profile = localRepo.getCurrentProfile()
                remoteRepo.saveUserProfile(
                    profile,
                    onSuccess = {
                        Log.i(TAG, "confirmEmailUpdate: Email synced to Firestore")
                        Toast.makeText(context, "Email updated successfully", Toast.LENGTH_SHORT).show()
                    },
                    onError = { e ->
                        Log.e(
                            TAG,
                            "confirmEmailUpdate: Failed to sync email to Firestore, error=${e.message}"
                        )
                        Toast.makeText(
                            context,
                            "Failed to sync email: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )

                user.sendEmailVerification().addOnSuccessListener {
                    Log.i(TAG, "confirmEmailUpdate: Verification email sent to $newEmail")
                    Toast.makeText(
                        context,
                        "A verification email has been sent to $newEmail.",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener { e ->
                    Log.e(TAG, "confirmEmailUpdate: Failed to send verification email, error=${e.message}")
                    Toast.makeText(
                        context,
                        "Failed to send verification email: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "confirmEmailUpdate: error=${e.message}, stacktrace=${e.stackTraceToString()}")
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                    is FirebaseAuthException -> when (e.errorCode) {
                        "ERROR_REQUIRES_RECENT_LOGIN" -> "Please log out and log in again to update your email."
                        "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already in use by another account."
                        "ERROR_INVALID_EMAIL" -> "Invalid email format."
                        "ERROR_OPERATION_NOT_ALLOWED" -> "Email updates are not allowed. Please check your Firebase Authentication settings."
                        else -> "Failed to update email: ${e.message}"
                    }
                    else -> "Failed to update email: ${e.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getCurrentEmail(): String {
        return FirebaseAuth.getInstance().currentUser?.email ?: "No email"
    }

    fun toggleNotifications(enabled: Boolean) {
        Log.d(TAG, "toggleNotifications: enabled=$enabled")
        localRepo.toggleNotifications(enabled)
        syncProfileToFirebase()
        Log.i(TAG, "toggleNotifications: success")
    }

    fun toggleTheme() {
        Log.d(TAG, "toggleTheme")
        localRepo.toggleTheme()
        syncProfileToFirebase()
        Log.i(TAG, "toggleTheme: success")
    }

    fun onImageSelected(uri: Uri, context: Context) {
        Log.d(TAG, "onImageSelected: uri=$uri")
        viewModelScope.launch {
            val uriString = uri.toString()
            if (uriString.isNotEmpty()) {
                localRepo.saveProfileImageUri(context, uriString)
                syncProfileToFirebase()
                Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
            } else {
                Log.w(TAG, "onImageSelected: Invalid or empty URI")
                Toast.makeText(context, "Failed to update profile picture: Invalid image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun syncProfileToFirebase() {
        Log.d(TAG, "syncProfileToFirebase")
        viewModelScope.launch {
            val profile = localRepo.getCurrentProfile()
            remoteRepo.saveUserProfile(
                profile,
                onSuccess = { Log.i(TAG, "syncProfileToFirebase: success") },
                onError = { e -> Log.e(TAG, "syncProfileToFirebase: error=${e.message}") }
            )
        }
    }

    fun changePasswordWithVerification(
        context: Context,
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        Log.d(TAG, "changePasswordWithVerification")
        viewModelScope.launch {
            try {
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email ?: throw IllegalStateException("User not logged in")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, oldPassword).await()
                remoteRepo.changePassword(
                    newPassword,
                    onSuccess = {
                        Log.i(TAG, "changePasswordWithVerification: success")
                        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                        onSuccess()
                    },
                    onError = { e ->
                        Log.e(TAG, "changePasswordWithVerification: error=${e.message}")
                        onError(e)
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "changePasswordWithVerification: error=${e.message}")
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, "Incorrect old password", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        onError(e)
                    }
                }
            }
        }
    }

    fun logout() {
        Log.d(TAG, "logout")
        viewModelScope.launch {
            remoteRepo.logout()
            Log.i(TAG, "logout: success")
        }
    }

    fun deleteAccount() {
        Log.d(TAG, "deleteAccount")
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                FirebaseFirestore.getInstance().collection("users").document(userId).delete()
                    .await()
                remoteRepo.deleteAccount(
                    onSuccess = { Log.i(TAG, "deleteAccount: success") },
                    onError = { e -> Log.e(TAG, "deleteAccount: error=${e.message}") }
                )
            } catch (e: Exception) {
                Log.e(TAG, "deleteAccount: error=${e.message}")
            }
        }
    }

    fun resetData() {
        Log.d(TAG, "resetData")
        localRepo.updateName("Guest")
        localRepo.updateEmail("")
        localRepo.updateProfileImage("")
        localRepo.toggleNotifications(true)
        if (isDarkTheme.value) localRepo.toggleTheme()
        syncProfileToFirebase()
        Log.i(TAG, "resetData: success")
    }
}