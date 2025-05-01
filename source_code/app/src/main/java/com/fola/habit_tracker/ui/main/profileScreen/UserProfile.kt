package com.fola.habit_tracker.ui.main.profileScreen

import androidx.annotation.DrawableRes

data class UserProfile(
    val name: String = "No Name",
    val profileImageUri: String = "",
    val notificationsEnabled: Boolean = true,
    val darkTheme: Boolean = false
)
