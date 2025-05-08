package com.fola.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.fola.habit_tracker.ui.auth.AuthNavigation
import com.fola.habit_tracker.ui.main.navigation_bar.MainApp
import com.fola.habit_tracker.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {

                // الحالة التي تحدد إذا كان المستخدم مسجل دخول أم لا
                val isLoggedIn = remember {
                    mutableStateOf(
                        Firebase.auth.currentUser?.isEmailVerified == true
                    )
                }

                if (isLoggedIn.value) {
                    MainApp()
                } else {
                    AuthNavigation(
                        onAuthSuccess = {
                            isLoggedIn.value = true
                        }
                    )
                }
            }
        }
    }
}

