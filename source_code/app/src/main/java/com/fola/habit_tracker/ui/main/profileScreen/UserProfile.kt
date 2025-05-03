package com.fola.habit_tracker.ui.main.profileScreen


data class UserProfile(
    val name: String = "",
    val profileImageUri: String = "",
    val notificationsEnabled: Boolean = true,
    val darkTheme: Boolean = false
)
