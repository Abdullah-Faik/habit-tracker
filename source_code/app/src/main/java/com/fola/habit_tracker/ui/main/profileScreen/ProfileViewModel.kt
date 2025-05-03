package com.fola.habit_tracker.ui.main.profileScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    fun editProfileDetails() {
        Log.d(TAG, "editProfileDetails")
        Log.i(TAG, "editProfileDetails: initiated")
    }

    fun onNameChanged(newName: String) {
        Log.d(TAG, "onNameChanged: name=$newName")
        localRepo.updateName(newName)
        syncProfileToFirebase()
        Log.i(TAG, "onNameChanged: success")
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

    fun changePassword(newPassword: String) {
        Log.d(TAG, "changePassword")
        viewModelScope.launch {
            remoteRepo.changePassword(
                newPassword,
                onSuccess = { Log.i(TAG, "changePassword: success") },
                onError = { e -> Log.e(TAG, "changePassword: error=${e.message}") }
            )
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
            remoteRepo.deleteAccount(
                onSuccess = { Log.i(TAG, "deleteAccount: success") },
                onError = { e -> Log.e(TAG, "deleteAccount: error=${e.message}") }
            )
        }
    }

    fun resetData() {
        Log.d(TAG, "resetData")
        localRepo.updateName("")
        localRepo.updateProfileImage("")
        localRepo.toggleNotifications(true)
        if (isDarkTheme.value) localRepo.toggleTheme()
        syncProfileToFirebase()
        Log.i(TAG, "resetData: success")
    }
}