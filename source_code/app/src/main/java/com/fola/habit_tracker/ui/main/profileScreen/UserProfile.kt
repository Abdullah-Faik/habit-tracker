package com.fola.habit_tracker.ui.main.profileScreen


data class UserProfile(
    val name: String = "",
    val email : String = "",
    val Password: String = "",
    val profileImageUri: String = "",
    val notificationsEnabled: Boolean = false,
    val darkTheme: Boolean = true
)
