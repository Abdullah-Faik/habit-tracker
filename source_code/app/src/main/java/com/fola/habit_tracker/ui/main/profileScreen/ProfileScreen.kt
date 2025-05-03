package com.fola.habit_tracker.ui.main.profileScreen

import android.net.Uri
import com.fola.habit_tracker.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.fola.habit_tracker.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()
    var localDarkTheme by remember { mutableStateOf(isDarkTheme) }

    AppTheme(darkTheme = localDarkTheme) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfilePicture(userProfile.profileImageUri) { uri ->
                                viewModel.onImageSelected(uri)
                            }
                            Spacer(modifier = Modifier.padding(12.dp))
                            Text(
                                text = userProfile.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingsMenu(
                        onItemClick = { item ->
                            when (item) {
                                "Edit Profile Details" -> viewModel.editProfileDetails()
                                "Change Password" -> viewModel.changePassword("new-password")
                                "Logout" -> viewModel.logout()
                                "Notifications" -> viewModel.toggleNotifications(!isNotificationsEnabled)
                                "Dark Theme" -> {
                                    localDarkTheme = !localDarkTheme
                                    viewModel.toggleTheme()
                                }
                                "Reset Data" -> viewModel.resetData()
                                "Delete Account" -> viewModel.deleteAccount()
                            }
                        },
                        isDarkTheme = localDarkTheme,
                        isNotificationsEnabled = isNotificationsEnabled
                    )
                }
            }
        }
    }
}

@Composable
fun ProfilePicture(imageUri: String, onEditClick: (Uri) -> Unit) {
    Box(
        modifier = Modifier.size(110.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (imageUri.isNotEmpty()) imageUri else R.drawable.def)
                .size(Size(100, 100))
                .build()
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
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    shape = CircleShape
                )
        )

        FloatingActionButton(
            onClick = {
                // TODO: استدعاء Image Picker هنا وتمرير Uri إلى onEditClick
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
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
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
            fontSize = 14.sp,
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
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
        ) {
            item {
                Text(
                    text = "Account",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(
                listOf(
                    Pair("Edit Profile Details", Icons.Default.PersonAdd),
                    Pair("Change Password", Icons.Default.Lock),
                    Pair("Logout", Icons.Default.Person)
                )
            ) { (text, icon) ->
                SettingsMenuItem(
                    text = text,
                    icon = icon,
                    onClick = { onItemClick(text) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Preferences",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(
                listOf(
                    Pair("Notifications", Icons.Default.Notifications),
                    Pair("Dark Theme", Icons.Default.Palette)
                )
            ) { (text, icon) ->
                SettingsMenuItem(
                    text = text,
                    icon = icon,
                    isSwitchEnabled = if (text == "Notifications") isNotificationsEnabled else isDarkTheme,
                    onSwitchChange = { enabled ->
                        onItemClick(text)
                    },
                    onClick = { onItemClick(text) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Data",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(
                listOf(
                    Pair("Reset Data", Icons.Default.Warning),
                    Pair("Delete Account", Icons.Default.Warning)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileScreen(
            viewModel = ProfileViewModel(
                localRepo = LocalProfileRepository(),
                remoteRepo = RemoteProfileRepository()
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileScreen(
            viewModel = ProfileViewModel(
                localRepo = LocalProfileRepository(),
                remoteRepo = RemoteProfileRepository()
            )
        )
    }
}