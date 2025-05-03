package com.fola.habit_tracker.ui.main.profileScreen

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RemoteProfileRepository {
    private val TAG = "RemoteProfileRepo"
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid ?: "default_user"
    private val defaultProfileImageUri = "https://firebasestorage.googleapis.com/.../default_profile.jpg" // استبدل بالرابط الفعلي

    fun loadUserProfile(onSuccess: (UserProfile) -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "loadUserProfile: userId=$userId")
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    doc.toObject(UserProfile::class.java)?.let {
                        Log.i(TAG, "loadUserProfile: success")
                        onSuccess(it)
                    }
                } else {
                    Log.d(TAG, "loadUserProfile: creating default profile")
                    val defaultProfile = UserProfile(
                        name = auth.currentUser?.displayName ?: "مستخدم جديد",
                        profileImageUri = defaultProfileImageUri,
                        notificationsEnabled = true,
                        darkTheme = false
                    )
                    saveUserProfile(
                        defaultProfile,
                        onSuccess = {
                            Log.i(TAG, "loadUserProfile: default profile saved")
                            onSuccess(defaultProfile)
                        },
                        onError = { e ->
                            Log.e(TAG, "loadUserProfile: error=${e.message}")
                            onError(e)
                        }
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "loadUserProfile: error=${e.message}")
                onError(e)
            }
    }

    fun saveUserProfile(profile: UserProfile, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "saveUserProfile: userId=$userId")
        firestore.collection("users").document(userId)
            .set(profile)
            .addOnSuccessListener {
                Log.i(TAG, "saveUserProfile: success")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "saveUserProfile: error=${e.message}")
                onError(e)
            }
    }

    fun uploadProfileImage(uri: Uri, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "uploadProfileImage: userId=$userId, uri=$uri")
        val ref = storage.child("profile_pics/$userId.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    Log.i(TAG, "uploadProfileImage: success")
                    onSuccess(downloadUri.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "uploadProfileImage: error=${e.message}")
                onError(e)
            }
    }

    fun logout() {
        Log.d(TAG, "logout: userId=$userId")
        auth.signOut()
        Log.i(TAG, "logout: success")
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "deleteAccount: userId=$userId")
        auth.currentUser?.delete()
            ?.addOnSuccessListener {
                Log.i(TAG, "deleteAccount: success")
                onSuccess()
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "deleteAccount: error=${e.message}")
                onError(e)
            }
    }

    fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        Log.d(TAG, "changePassword: userId=$userId")
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                Log.i(TAG, "changePassword: success")
                onSuccess()
            }
            ?.addOnFailureListener { e ->
                Log.e(TAG, "changePassword: error=${e.message}")
                onError(e)
            }
    }
}