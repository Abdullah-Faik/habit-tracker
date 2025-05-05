package com.fola.habit_tracker.ui.main.profileScreen

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RemoteProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: "default_user"

    // Firebase Firestore: تحميل بيانات المستخدم من قاعدة البيانات //
    fun loadUserProfile(
        onSuccess: (UserProfile) -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->
                doc.toObject(UserProfile::class.java)?.let { onSuccess(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    // Firebase Firestore: حفظ بيانات المستخدم //
    fun saveUserProfile(
        profile: UserProfile,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("users").document(userId)
            .set(profile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // Firebase Storage: رفع صورة الملف الشخصي //
    fun uploadProfileImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val ref = storage.child("profile_pics/$userId.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString()) // Firebase Storage: إرسال رابط الصورة
                }
            }
            .addOnFailureListener(onError)
    }

    // Firebase Auth: تسجيل الخروج
    fun logout() {
        auth.signOut()
    }

    // Firebase Auth: حذف الحساب
    fun deleteAccount(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        auth.currentUser?.delete()
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onError(it) }
    }

    // Firebase Auth: تغيير كلمة المرور (مثال)
    fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onError(it) }
    }
}
