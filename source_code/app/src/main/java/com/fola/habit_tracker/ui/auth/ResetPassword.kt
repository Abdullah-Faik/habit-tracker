package com.fola.habit_tracker.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.auth.viewmodel.ResetPasswordViewmodel
import com.fola.habit_tracker.ui.components.InputField
import com.fola.habit_tracker.ui.components.StyledButton
import com.fola.habit_tracker.ui.theme.AppTheme

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit = {},
    resetViewmodel: ResetPasswordViewmodel = viewModel()
) {

    val uiState = resetViewmodel.emailState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = resetViewmodel) {
        resetViewmodel.snackbarEvent.collect { message ->
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
        },
        topBar = {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp, start = 16.dp)
                    .clickable(onClick = onBackButton)
            ) {
                Image(
                    painter = painterResource(R.drawable.back_step),
                    contentDescription = "back",
                    modifier = Modifier.size(56.dp)

                )
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 36.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Reset Your Password",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp)

            ) {
                InputField(
                    value = uiState.value.text,
                    onValueChange = { value -> resetViewmodel.updateEmail(value) },
                    uiState = uiState.value.state,
                    errorText = uiState.value.errorMessage,
                    placeholder = "Email"
                )
                StyledButton(
                    onClick = { resetViewmodel.resetPassword() },
                    text = "Reset"
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Already have an account?",
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton (
                    onClick = onBackButton,
                ) {
                    Text(
                        text = "Sign In",
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                    )
                }
            }
        }
    }


}


@Preview
@Composable
private fun ResetPasswordPrev() {
    AppTheme {
        ResetPasswordScreen()
    }
}