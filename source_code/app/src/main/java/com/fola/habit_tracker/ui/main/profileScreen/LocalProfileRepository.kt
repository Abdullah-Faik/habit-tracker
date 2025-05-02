package com.fola.habit_tracker.ui.main.profileScreen

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalProfileRepository {

    // Local: الحالة الحالية للمستخدم
    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Mostafa Ali",
            profileImageUri = "",
            notificationsEnabled = false,
            darkTheme = false
        )
    )
    val userProfile = _userProfile.asStateFlow()

    // Local: تفعيل الإشعارات
    private val _isNotificationsEnabled = MutableStateFlow(false)
    val isNotificationsEnabled = _isNotificationsEnabled.asStateFlow()

    // Local: تفعيل الوضع الليلي
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    // Local: تحديث الاسم داخل التطبيق
    fun updateName(name: String) {
        _userProfile.value = _userProfile.value.copy(name = name)
    }

    // Local: تحديث رابط الصورة داخل التطبيق
    fun updateProfileImage(uri: String) {
        _userProfile.value = _userProfile.value.copy(profileImageUri = uri)
    }

    // Local: تفعيل/تعطيل الإشعارات داخل التطبيق
    fun toggleNotifications(enabled: Boolean) {
        _isNotificationsEnabled.value = enabled
        _userProfile.value = _userProfile.value.copy(notificationsEnabled = enabled)
    }

    // Local: تفعيل/إيقاف الوضع الليلي داخل التطبيق
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        _userProfile.value = _userProfile.value.copy(darkTheme = _isDarkTheme.value)
    }

    // Local: الحصول على النسخة الحالية من بيانات المستخدم
    fun getCurrentProfile(): UserProfile = _userProfile.value
}
