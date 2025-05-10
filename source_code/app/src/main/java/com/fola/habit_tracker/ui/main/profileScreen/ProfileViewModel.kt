package com.fola.habit_tracker.ui.main.profileScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val localRepo: LocalProfileRepository,
    private val remoteRepo: RemoteProfileRepository
) : ViewModel() {

    // ✅ المراقبة المحلية لحالة الملف الشخصي (اسم - صورة - إعدادات)
    open val userProfile: StateFlow<UserProfile> = localRepo.userProfile

    // ✅ الإشعارات والوضع الليلي مراقَبة محليًا
    open val isNotificationsEnabled: StateFlow<Boolean> = localRepo.isNotificationsEnabled
    open val isDarkTheme: StateFlow<Boolean> = localRepo.isDarkTheme

    init {
        loadUserProfileFromFirebase()
    }

    // ☁️ تحميل البيانات من Firestore إلى LocalRepository عند بداية التشغيل
    private fun loadUserProfileFromFirebase() {
        viewModelScope.launch {
            remoteRepo.loadUserProfile(
                onSuccess = { remoteProfile ->
                    // ✅ تحديث البيانات محليًا من Firebase
                    localRepo.updateName(remoteProfile.name)
                    localRepo.updateProfileImage(remoteProfile.profileImageUri)
                    localRepo.toggleNotifications(remoteProfile.notificationsEnabled)
                    if (remoteProfile.darkTheme != isDarkTheme.value) {
                        localRepo.toggleTheme()
                    }
                },
                onError = {
                    // ⚠️ يمكنك عرض رسالة خطأ هنا أو الاحتفاظ بالبيانات الحالية
                }
            )
        }
    }


    // for editing user profile data
    fun editProfileDetails(){

    }
    // 🟡 عند تعديل اسم المستخدم، يحدث محليًا ويتم مزامنته مع Firebase
    fun onNameChanged(newName: String) {
        localRepo.updateName(newName)
        syncProfileToFirebase()
    }

    // 🟡 تحديث إعداد الإشعارات محليًا + حفظه في Firebase
    fun toggleNotifications(enabled: Boolean) {
        localRepo.toggleNotifications(enabled)
        syncProfileToFirebase()
    }

    // 🟡 تبديل الوضع الليلي وتحديثه في Firestore
    fun toggleTheme() {
        localRepo.toggleTheme()
        syncProfileToFirebase()
    }

    // ☁️ رفع صورة جديدة إلى Firebase Storage ثم تحديث الرابط محليًا + Firestore
    fun onImageSelected(uri: Uri) {
        viewModelScope.launch {
            remoteRepo.uploadProfileImage(
                uri,
                onSuccess = { downloadUrl ->
                    localRepo.updateProfileImage(downloadUrl)
                    syncProfileToFirebase()
                },
                onError = {
                    // ⚠️ عرض رسالة فشل رفع الصورة
                }
            )
        }
    }

    // ☁️ مزامنة البيانات الحالية مع Firestore
    private fun syncProfileToFirebase() {
        viewModelScope.launch {
            val profile = localRepo.getCurrentProfile()
            remoteRepo.saveUserProfile(
                profile,
                onSuccess = { /* ✅ تم الحفظ بنجاح */ },
                onError = { /* ⚠️ حدث خطأ أثناء الحفظ */ }
            )
        }
    }

    // ☁️ تغيير كلمة المرور عبر Firebase Auth
    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            remoteRepo.changePassword(
                newPassword,
                onSuccess = { /* ✅ تم تغيير كلمة المرور */ },
                onError = { /* ⚠️ فشل تغيير كلمة المرور */ }
            )
        }
    }

    // ☁️ تسجيل الخروج باستخدام Firebase Auth
    fun logout() {
        viewModelScope.launch {
            remoteRepo.logout()
            // يمكنك التنقل إلى شاشة تسجيل الدخول بعد ذلك
        }
    }

    // ☁️ حذف الحساب تمامًا من Firebase Auth و Firestore
    fun deleteAccount() {
        viewModelScope.launch {
            remoteRepo.deleteAccount(
                onSuccess = {
                    // ✅ حذف الحساب بنجاح، يمكن التنقل إلى شاشة البداية
                },
                onError = {
                    // ⚠️ فشل في حذف الحساب
                }
            )
        }
    }


    fun resetData() {
        localRepo.updateName("")
        localRepo.updateProfileImage("")
        localRepo.toggleNotifications(true)
        if (isDarkTheme.value) localRepo.toggleTheme()
        syncProfileToFirebase()
    }
}


