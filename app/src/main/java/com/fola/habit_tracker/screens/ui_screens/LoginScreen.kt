package com.fola.habit_tracker.screens.ui_screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.basic_background))
            .padding(horizontal = 16.dp),
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

        InputField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            uiState = UiState.IDLE,
            keyboardType = KeyboardType.Email
        )

        PasswordInputField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Password",
            uiState = UiState.IDLE
        )

        StyledButton(
            onClick = { /* TODO: Handle sign-in */ },
            text = "Sign In"
        )

        IconButton(
            onClick = { /* TODO: Handle forgot password */ },
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
            "Don't Have an Account?",
            color = Color.White,
            modifier = Modifier.padding(12.dp)
        )

        StyledButton(
            onClick = { /* TODO: Handle create account */ },
            text = "Create Account"
        )
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
            modifier = Modifier.weight(1f)
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
    }
}

@Composable
fun SocialLoginButton(onClick: () -> Unit, @DrawableRes icon: Int, text: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Text(text, color = Color.Black)
        }
    }
}

@Preview
@Composable
private fun LoginPrev() {
    LoginScreen()
}
