package com.fola.habit_tracker.ui.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.fola.habit_tracker.ui.auth.viewmodel.RegisterViewmodel
import com.fola.habit_tracker.ui.components.InputField
import com.fola.habit_tracker.ui.components.PasswordInputField
import com.fola.habit_tracker.ui.components.StyledButton


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ResourceType", "StateFlowValueCalledInComposition")
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit = {},
    registerViewmodel: RegisterViewmodel = viewModel()
) {
    val emailState = registerViewmodel.emailState.collectAsState()
    val nameState = registerViewmodel.nameState.collectAsState()
    val password = registerViewmodel.password.collectAsState()
    val rePassword = registerViewmodel.rePassword.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = registerViewmodel) {
        registerViewmodel.snackbarEvent.collect { message ->
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
            TopAppBar(
                title = {},
                navigationIcon = {
                    Box(
                        Modifier
                            .clickable(
                                onClick = onBackButton
                            )
                            .padding(start = 8.dp)
                    )
                    {
                        Image(
                            painter = painterResource(R.drawable.back_step),
                            contentDescription = "",
                            modifier = Modifier.size(56.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.basic_background)
                )

            )
        }

    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .background(color = colorResource(R.color.basic_background))
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Your Account",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 36.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp),
                modifier = Modifier
                    .padding(vertical = 18.dp)
            ) {
                InputField(
                    value = nameState.value.text,
                    onValueChange = { value -> registerViewmodel.updateName(value) },
                    placeholder = "Name",
                    errorText = nameState.value.errorMessage,
                    uiState = nameState.value.state

                )
                InputField(
                    value = emailState.value.text,
                    onValueChange = { value -> registerViewmodel.updateEmail(value) },
                    placeholder = "Email",
                    errorText = emailState.value.errorMessage,
                    uiState = emailState.value.state
                )
                PasswordInputField(
                    value = password.value.text,
                    onValueChange = { value -> registerViewmodel.updatePassword(value) },
                    placeholder = "Password",
                    errorMessage = password.value.errorMessage,
                    uiState = password.value.state,
                )
                PasswordInputField(
                    value = rePassword.value.text,
                    onValueChange = { value -> registerViewmodel.updateRePassword(value) },
                    placeholder = "Confirm Password",
                    errorMessage = rePassword.value.errorMessage,
                    uiState = rePassword.value.state
                )
            }
            StyledButton(
                onClick = { registerViewmodel.addNewUser() },
                text = "Sign Up"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    "Already have an account?",
                    color = Color.Gray
                )
                IconButton(
                    onClick = onBackButton,
                    Modifier.width(IntrinsicSize.Min)
                ) {
                    Text(
                        text = "Sign In",
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun RegisterPrev() {
    RegisterScreen()
}