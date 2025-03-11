package com.fola.habit_tracker.ui.auth

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.auth.viewmodel.LoginViewmodel
import com.fola.habit_tracker.ui.components.InputField
import com.fola.habit_tracker.ui.components.PasswordInputField
import com.fola.habit_tracker.ui.components.StyledButton

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onCreateAccount: () -> Unit = {},
    onForgetPassword: () -> Unit = {},
    loginViewmodel: LoginViewmodel = viewModel()
) {
    val loginState = loginViewmodel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = loginViewmodel) {
        loginViewmodel.snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(
                message,
                duration = SnackbarDuration.Long
            )
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState, snackbar = {
                Snackbar(
                    snackbarData = it,
                    containerColor = colorResource(R.color.main_color),
                    contentColor = colorResource(R.color.black),
                )

            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.basic_background))
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Sign in",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier.padding(vertical = 18.dp)
            )
            {

                InputField(
                    value = loginState.value.email.text,
                    onValueChange = { email -> loginViewmodel.updateEmailField(email) },
                    placeholder = "Email",
                    errorText = loginState.value.email.errorMessage,
                    uiState = loginState.value.email.state,
                    keyboardType = KeyboardType.Email
                )

                PasswordInputField(
                    value = loginState.value.password.text,
                    onValueChange = { loginViewmodel.updatePassword(it) },
                    placeholder = "Password",
                    errorMessage = loginState.value.password.errorMessage,
                    uiState = loginState.value.password.state
                )

                StyledButton(
                    onClick = { loginViewmodel.signIn() },
                    text = "Sign In"
                )
            }

            IconButton(
                onClick = onForgetPassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Forget Password?",
                    color = colorResource(R.color.main_color)
                )
            }

            SocialLoginButtons()

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                "Don't have an account?",
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )

            StyledButton(
                onClick = onCreateAccount,
                text = "Create account"
            )
        }
    }
}

@Composable
fun SocialLoginButtons() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SocialLoginButton(
                onClick = { /* TODO: Handle Google login */ },
                icon = R.drawable.google,
                text = "Google"
            )
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            SocialLoginButton(
                onClick = { /* TODO: Handle Facebook login */ },
                icon = R.drawable.facebook,
                text = "Facebook"
            )
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            SocialLoginButton(
                onClick = {},
                image = Icons.Default.Person,
                text = "Guest"
            )
        }
    }

}

@Composable
fun SocialLoginButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int? = null,
    text: String,
    image: ImageVector? = null
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            if (icon != null)
                Image(
                    painter = painterResource(icon),
                    contentDescription = text,
                    modifier = Modifier.size(24.dp)
                ) else if (image != null) {
                Image(image, contentDescription = "text", Modifier.size(24.dp))
            }
            Text(text, color = Color.Black, fontSize = 14.sp)
        }
    }

}

@Preview
@Composable
private fun LoginPrev() {
    LoginScreen()
}
