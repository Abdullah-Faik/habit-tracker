package com.fola.habit_tracker.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.auth.viewmodel.AuthScreen


@Composable
fun AuthNavigation(modifier: Modifier = Modifier, onAuthSuccess: () -> Unit) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthScreen.START.name,
        modifier = modifier
    ) {
        composable(route = AuthScreen.LOGIN.name)
        {
            LoginScreen(
                onForgetPassword = {
                    navController.navigate(AuthScreen.RESET_PASSWORD.name) {
                        launchSingleTop = true
                    }
                },
                onCreateAccount = {
                    navController.navigate(AuthScreen.REGISTER.name) {
                        launchSingleTop = true
                    }
                },
                onLoginSuccess = {
                    onAuthSuccess() // ✅ هنا ننتقل إلى MainApp بعد نجاح تسجيل الدخول
                }

            )

        }
        composable(route = AuthScreen.START.name) {
            WelcomeScreen(
                onStartButton = {
                    navController.navigate(AuthScreen.LOGIN.name)
                    {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AuthScreen.REGISTER.name) {
            RegisterScreen(
                onBackButton = {
                    navController.popBackStack(AuthScreen.LOGIN.name, inclusive = false)

                }
            )
        }
        composable(route = AuthScreen.RESET_PASSWORD.name) {
            ResetPasswordScreen(
                onBackButton = {
                    navController.popBackStack(AuthScreen.LOGIN.name, inclusive = false)
                }
            )
        }
    }


}

@Preview
@Composable
fun Preiv() {
    AuthNavigation(onAuthSuccess = {})
}