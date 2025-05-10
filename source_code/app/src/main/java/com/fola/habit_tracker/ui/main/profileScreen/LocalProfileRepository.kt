package com.fola.habit_tracker.ui.main.profileScreen

import android.content.Context
import android.content.SharedPreferences
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

    fun getSharedPreferences(context: Context = getApplicationContext()): SharedPreferences {
        return context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
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

    fun loadProfileImageUri(context: Context) {
        Log.d(TAG, "loadProfileImageUri")
        val sharedPreferences = getSharedPreferences(context)
        val savedUri = sharedPreferences.getString("profile_image_uri", "")
        if (savedUri != null) {
            Log.i(TAG, "loadProfileImageUri: Restoring URI=$savedUri")
            _userProfile.value = _userProfile.value.copy(profileImageUri = savedUri)
        }
    }

    fun saveProfileImageUri(context: Context, uri: String) {
        Log.d(TAG, "saveProfileImageUri: uri=$uri")
        val sharedPreferences = getSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString("profile_image_uri", uri)
            apply()
        }
        _userProfile.value = _userProfile.value.copy(profileImageUri = uri)
        Log.i(TAG, "saveProfileImageUri: success")
    }

    fun updateName(name: String) {
        Log.d(TAG, "updateName: name=$name")
        _userProfile.value = _userProfile.value.copy(name = name)
        Log.i(TAG, "updateName: success")
    }

    fun updateEmail(newEmail: String) {
        Log.d(TAG, "updateEmail: email=$newEmail")
        _userProfile.value = _userProfile.value.copy(email = newEmail)
        Log.i(TAG, "updateEmail: success")
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