package com.fola.habit_tracker.ui.profileScreen

import com.fola.habit_tracker.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileRepository {

    private val _userProfile = MutableStateFlow(UserProfile("Merit Hamed", R.drawable.avatar))
    private val _isNotificationsEnabled = MutableStateFlow(true)

    fun getUserProfile(): Flow<UserProfile> = _userProfile

    fun getNotificationsEnabled(): Flow<Boolean> = _isNotificationsEnabled

    fun toggleNotifications(enabled: Boolean) {
        _isNotificationsEnabled.value = enabled
        // Add logic to save notification settings (e.g., to SharedPreferences or database)
    }

    fun updateUserProfile(name: String, profileImageRes: Int) {
        _userProfile.value = UserProfile(name, profileImageRes)
    }
}