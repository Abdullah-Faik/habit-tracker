package com.fola.habit_tracker.ui.main.profileScreen

import androidx.annotation.DrawableRes

data class UserProfile(
    val name: String = "Mostafa Ali",
    val profileImageUri: String = "",
    val notificationsEnabled: Boolean = true,
    val darkTheme: Boolean = false
)
