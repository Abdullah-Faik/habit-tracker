package com.fola.habit_tracker.ui.profileScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow


class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = repository.getUserProfile() as StateFlow<UserProfile>
    val isNotificationsEnabled: StateFlow<Boolean> = repository.getNotificationsEnabled() as StateFlow<Boolean>

    fun toggleNotifications(enabled: Boolean) {
        repository.toggleNotifications(enabled)
        // shared prefrences
    }

    fun changePassword() {
        // Logic to navigate to change password screen or show dialog
        println("Navigating to Change Password screen")
    }

    fun resetData() {
        // Reset data And navigate(home screen) and delete tasks & habits
        println("Resetting app data")
    }

    fun logout() {
        // Firebase Logout
        println("Logging out")
    }

    fun deleteAccount() {
        // Firebase delete account and Reset data
        println("Deleting account")
    }

    fun editProfile() {
        // Logic to navigate to edit profile screen
        println("Navigating to Edit Profile screen")
    }

}