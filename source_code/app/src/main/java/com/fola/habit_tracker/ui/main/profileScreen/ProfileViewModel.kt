package com.fola.habit_tracker.ui.main.profileScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.auth
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
        loadUserProfileFromFirebase()
    }

    private fun loadUserProfileFromFirebase() {
        Log.d(TAG, "loadUserProfileFromFirebase")
        viewModelScope.launch {
            remoteRepo.loadUserProfile(
                onSuccess = { remoteProfile ->
                    Log.i(TAG, "loadUserProfileFromFirebase: success")
                    localRepo.updateName(remoteProfile.name)
                    localRepo.updateProfileImage(remoteProfile.profileImageUri)
                    localRepo.toggleNotifications(remoteProfile.notificationsEnabled)
                    if (remoteProfile.darkTheme != isDarkTheme.value) {
                        localRepo.toggleTheme()
                    }
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

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                FirebaseAuth.getInstance().currentUser?.updateEmail(newEmail)?.await()
                Log.i(TAG, "updateEmail: success")
            } catch (e: Exception) {
                Log.e(TAG, "updateEmail: error=${e.message}")
            }
        }
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

    fun onImageSelected(uri: Uri) {
        Log.d(TAG, "onImageSelected: uri=$uri")
        viewModelScope.launch {
            remoteRepo.uploadProfileImage(
                uri,
                onSuccess = { downloadUrl ->
                    Log.i(TAG, "onImageSelected: success")
                    localRepo.updateProfileImage(downloadUrl)
                    syncProfileToFirebase()
                },
                onError = { e ->
                    Log.e(TAG, "onImageSelected: error=${e.message}")
                }
            )
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
        context: Context, // Add context parameter
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
                // Verify old password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, oldPassword).await()
                // Update password
                remoteRepo.changePassword(
                    newPassword,
                    onSuccess = {
                        Log.i(TAG, "changePasswordWithVerification: success")
                        // Show toast on success
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
                // Delete Firestore data
                FirebaseFirestore.getInstance().collection("users").document(userId).delete()
                    .await()
                // Delete Firebase Auth account
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
        localRepo.updateProfileImage("")
        localRepo.toggleNotifications(true)
        if (isDarkTheme.value) localRepo.toggleTheme()
        syncProfileToFirebase()
        Log.i(TAG, "resetData: success")
    }
}