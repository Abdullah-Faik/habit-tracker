package com.fola.habit_tracker.ui.main.profileScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsStateWithLifecycle()
    var currentEmail by remember { mutableStateOf(viewModel.getCurrentEmail()) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(isDarkModeEnabled(context)) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showConfirmEmailDialog by remember { mutableStateOf(false) }
    var pendingEmail by remember { mutableStateOf("") }
    var pendingPassword by remember { mutableStateOf("") }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImageSelected(it, context)
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfilePicture(
                        imageUri = userProfile.profileImageUri,
                        onEditClick = { imagePickerLauncher.launch("image/*") }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = userProfile.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = currentEmail,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Settings Menu
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                SettingsMenu(
                    onItemClick = { item ->
                        when (item) {
                            "Dark Theme" -> saveThemePref(context, !isDarkTheme)
                            "Notifications" -> viewModel.toggleNotifications(!isNotificationsEnabled)
                            "Logout" -> showLogoutDialog = true
                            "Delete Account" -> showDeleteDialog = true
                            "Reset Data" -> showResetDialog = true
                            "Edit Profile Details" -> showEditProfileDialog = true
                            "Change Password" -> showChangePasswordDialog = true
                        }
                    },
                    isDarkTheme = isDarkTheme,
                    isNotificationsEnabled = isNotificationsEnabled
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmation Dialogs
            if (showLogoutDialog) {
                ConfirmationDialog(
                    title = "Confirm Logout",
                    message = "Are you sure you want to log out?",
                    onConfirm = {
                        viewModel.logout()
                        showLogoutDialog = false
                    },
                    onDismiss = { showLogoutDialog = false }
                )
            }

            if (showDeleteDialog) {
                ConfirmationDialog(
                    title = "Confirm Delete Account",
                    message = "This action cannot be undone. Are you sure you want to delete your account?",
                    onConfirm = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    },
                    onDismiss = { showDeleteDialog = false },
                    confirmButtonColor = MaterialTheme.colorScheme.error
                )
            }

            if (showResetDialog) {
                ConfirmationDialog(
                    title = "Confirm Reset Data",
                    message = "This will clear all your habit data. Are you sure you want to proceed?",
                    onConfirm = {
                        viewModel.resetData()
                        showResetDialog = false
                    },
                    onDismiss = { showResetDialog = false },
                    confirmButtonColor = MaterialTheme.colorScheme.error
                )
            }

            if (showEditProfileDialog) {
                EditProfileDialog(
                    currentName = userProfile.name,
                    currentEmail = currentEmail,
                    onConfirm = { newName, newEmail, password ->
                        viewModel.onNameChanged(newName)
                        if (newEmail != currentEmail) {
                            if (password.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please write your current password before updating",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@EditProfileDialog
                            }
                            pendingEmail = newEmail
                            pendingPassword = password
                            viewModel.updateEmail(
                                newEmail,
                                password,
                                context,
                                onSuccess = {
                                    showConfirmEmailDialog = true
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            )
                        } else if (newEmail == currentEmail && newName == userProfile.name) {
                            Toast.makeText(context, "Please Update Your Email", Toast.LENGTH_SHORT)
                                .show()
                            showEditProfileDialog = true
                        } else {
                            showEditProfileDialog = false
                        }
                    },
                    onDismiss = { showEditProfileDialog = false }
                )
            }

            if (showConfirmEmailDialog) {
                ConfirmationDialog(
                    title = "Confirm Email Update",
                    message = "Have you verified the new email ($pendingEmail)?",
                    onConfirm = {
                        viewModel.confirmEmailUpdate(pendingEmail, pendingPassword, context)
                        currentEmail = viewModel.getCurrentEmail()
                        showConfirmEmailDialog = false
                        showEditProfileDialog = false
                        Toast.makeText(context, "Please Verify Your Email", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onDismiss = { showConfirmEmailDialog = false },
                    confirmButtonColor = MaterialTheme.colorScheme.primary
                )
            }

            if (showChangePasswordDialog) {
                ChangePasswordDialog(
                    onConfirm = { oldPassword, newPassword ->
                        viewModel.changePasswordWithVerification(
                            context = context,
                            oldPassword = oldPassword,
                            newPassword = newPassword,
                            onSuccess = { showChangePasswordDialog = false },
                            onError = { exception ->
                                val errorMessage = when (exception) {
                                    is FirebaseAuthInvalidCredentialsException -> "Incorrect old password"
                                    else -> "Failed to change password: ${exception.message}"
                                }
                                Toast.makeText(
                                    context,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    },
                    onDismiss = { showChangePasswordDialog = false }
                )
            }
        }
    }
}

// Rest of the file (ProfilePicture, SettingsMenuItem, etc.) remains unchanged

@Composable
fun ProfilePicture(
    imageUri: String,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(110.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = imageUri.takeIf { it.isNotEmpty() } ?: R.drawable.def,
            error = rememberAsyncImagePainter(R.drawable.def),
            placeholder = rememberAsyncImagePainter(R.drawable.def)
        )
        Image(
            painter = painter,
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = CircleShape
                )
        )

        FloatingActionButton(
            onClick = onEditClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(28.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit picture",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SettingsMenuItem(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    isSwitchEnabled: Boolean? = null,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (isSwitchEnabled != null && onSwitchChange != null) {
            Switch(
                checked = isSwitchEnabled,
                onCheckedChange = onSwitchChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
    isNotificationsEnabled: Boolean,
    isDarkTheme: Boolean
) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp)
        ) {
            item {
                SectionHeader("Account")
            }
            items(
                listOf(
                    "Edit Profile Details" to Icons.Default.PersonAdd,
                    "Change Password" to Icons.Default.Lock,
                    "Logout" to Icons.Default.Person
                )
            ) { (text, icon) ->
                SettingsMenuItem(
                    text = text,
                    icon = icon,
                    onClick = { onItemClick(text) }
                )
            }

            item {
                SectionHeader("Preferences")
            }
            items(
                listOf(
                    "Notifications" to Icons.Default.Notifications,
                    "Dark Theme" to Icons.Default.Palette
                )
            ) { (text, icon) ->
                SettingsMenuItem(
                    text = text,
                    icon = icon,
                    isSwitchEnabled = when (text) {
                        "Notifications" -> isNotificationsEnabled
                        "Dark Theme" -> isDarkTheme
                        else -> null
                    },
                    onSwitchChange = when (text) {
                        "Notifications" -> {
                            { onItemClick("Notifications") }
                        }

                        "Dark Theme" -> {
                            { onItemClick("Dark Theme") }
                        }

                        else -> null
                    },
                    onClick = { onItemClick(text) },
                    modifier = if (text == "Notifications") Modifier.padding(bottom = 2.dp) else Modifier
                )
            }

            item {
                SectionHeader("Data")
            }
            items(
                listOf(
                    "Reset Data" to Icons.Default.Warning,
                    "Delete Account" to Icons.Default.Warning
                )
            ) { (text, icon) ->
                SettingsMenuItem(
                    text = text,
                    icon = icon,
                    textColor = if (text == "Delete Account") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    onClick = { onItemClick(text) }
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmButtonColor: Color = MaterialTheme.colorScheme.primary
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = confirmButtonColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    onConfirm: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profile",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Current Password (required for email change)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, email, password) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
}

@Composable
fun ChangePasswordDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var oldPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Change Password",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true,
                    visualTransformation = if (oldPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { oldPasswordVisible = !oldPasswordVisible }) {
                            Icon(
                                imageVector = if (oldPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (oldPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = if (newPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(oldPassword, newPassword) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileScreen()
    }
}


fun saveThemePref(context: Context, isDarkMode: Boolean) {
    val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("dark_mode", isDarkMode).apply()
}

fun isDarkModeEnabled(context: Context): Boolean {
    val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("dark_mode", false) // false = light mode by default
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ProfileScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileScreen()
    }
}