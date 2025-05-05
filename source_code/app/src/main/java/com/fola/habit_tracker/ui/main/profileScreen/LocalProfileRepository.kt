package com.fola.habit_tracker.ui.main.profileScreen

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalProfileRepository {
    private val TAG = "LocalProfileRepo"

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "",
            email = "",
            profileImageUri = "",
            Password = "",
            notificationsEnabled = false,
            darkTheme = false

        )
    )
    val userProfile = _userProfile.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(false)
    val isNotificationsEnabled = _isNotificationsEnabled.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun updateName(name: String) {
        Log.d(TAG, "updateName: name=$name")
        _userProfile.value = _userProfile.value.copy(name = name)
        Log.i(TAG, "updateName: success")
    }

    fun updateEmail(newEmail: String) {
        _userProfile.value = _userProfile.value.copy(email = newEmail)
    }

    fun updatePassword(password: String) {
        Log.d(TAG, "updatePassword: password=$password")
        _userProfile.value = _userProfile.value.copy(Password = password)
        Log.i(TAG, "updatePassword: success")
    }

    fun updateProfileImage(uri: String) {
        Log.d(TAG, "updateProfileImage: uri=$uri")
        _userProfile.value = _userProfile.value.copy(profileImageUri = uri)
        Log.i(TAG, "updateProfileImage: success")
    }

    fun toggleNotifications(enabled: Boolean) {
        Log.d(TAG, "toggleNotifications: enabled=$enabled")
        _isNotificationsEnabled.value = enabled
        _userProfile.value = _userProfile.value.copy(notificationsEnabled = enabled)
        Log.i(TAG, "toggleNotifications: success")
    }

    fun toggleTheme() {
        Log.d(TAG, "toggleTheme: current=${_isDarkTheme.value}")
        _isDarkTheme.value = !_isDarkTheme.value
        _userProfile.value = _userProfile.value.copy(darkTheme = _isDarkTheme.value)
        Log.i(TAG, "toggleTheme: success")
    }

    fun getCurrentProfile(): UserProfile {
        Log.d(TAG, "getCurrentProfile")
        val profile = _userProfile.value
        Log.i(TAG, "getCurrentProfile: success")
        return profile
    }
}