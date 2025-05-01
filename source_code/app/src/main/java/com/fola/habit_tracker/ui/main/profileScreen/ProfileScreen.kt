package com.fola.habit_tracker.ui.main.profileScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.fola.habit_tracker.ui.theme.AppTheme
import android.net.Uri
import com.fola.habit_tracker.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImageSelected(it) // Convert string to Uri and call onImageSelected
        }
    }

    val entranceAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

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
                modifier = Modifier
                    .scale(entranceAnim)
                    .alpha(entranceAnim),
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
                        ProfilePicture(
                            userProfile = userProfile,
                            onEditClick = { imagePickerLauncher.launch("image/*") }
                        )

                        Spacer(modifier = modifier.padding(12.dp))

                        Text(
                            text = userProfile.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingsMenu(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    viewModel = viewModel,
                    isNotificationsEnabled = isNotificationsEnabled,
                    onToggleNotifications = { viewModel.toggleNotifications(it) }
                )
            }
        }
    }
}

@Composable
fun ProfilePicture(userProfile: UserProfile, onEditClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(
        model = if (userProfile.profileImageUri.isNotEmpty()) userProfile.profileImageUri else null, // Avoid loading empty URI
        placeholder = painterResource(R.drawable.avatar), // Use custom avatar as placeholder
        error = painterResource(R.drawable.avatar), // Use custom avatar as error image
        contentScale = ContentScale.Crop
    )

    val pulseAnim by animateFloatAsState(
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(110.dp)
            .scale(pulseAnim)
    ) {
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
                .shadow(4.dp, CircleShape)
        )

        FloatingActionButton(
            onClick = onEditClick,
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    toggle: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100)
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent,
        animationSpec = tween(200)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = if (toggle == null) onClick else {
                    {}
                }
            )
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
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
        if (toggle != null) {
            toggle()
        }
    }
}

@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    isNotificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp)
        ) {
            // Account Section
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
                    Triple(
                        "Edit Profile Details",
                        Icons.Default.PersonAdd,
                        { viewModel.editProfileDetails() }),
                    Triple("Change Password", Icons.Default.Lock, { viewModel.changePassword("") }), // Temporary empty string
                    Triple("Logout", Icons.Default.Person, { viewModel.logout() })
                )
            ) { (text, icon, onClick) ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300))
                ) {
                    SettingsMenuItem(
                        text = text,
                        icon = icon,
                        textColor = MaterialTheme.colorScheme.onSurface,
                        onClick = onClick
                    )
                }
            }

            // Preferences Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                    Triple("Notifications", Icons.Default.Notifications, {}),
                    Triple("Theme Settings", Icons.Default.Palette, { viewModel.toggleTheme() }),
                )
            ) { (text, icon, onClick) ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300))
                ) {
                    if (text == "Notifications") {
                        SettingsMenuItem(
                            text = text,
                            icon = icon,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            onClick = onClick,
                            toggle = {
                                Switch(
                                    checked = isNotificationsEnabled,
                                    onCheckedChange = onToggleNotifications,
                                    modifier = Modifier.scale(
                                        animateFloatAsState(
                                            if (isNotificationsEnabled) 1.05f else 1f,
                                            spring()
                                        ).value
                                    ),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.3f
                                        )
                                    )
                                )
                            }
                        )
                    } else {
                        SettingsMenuItem(
                            text = text,
                            icon = icon,
                            textColor = MaterialTheme.colorScheme.onSurface,
                            onClick = onClick
                        )
                    }
                }
            }

            // Data Section
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
                    Triple("Reset Data", Icons.Default.Warning, { viewModel.resetData() }),
                    Triple("Delete Account", Icons.Default.Warning, { viewModel.deleteAccount() })
                )
            ) { (text, icon, onClick) ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300))
                ) {
                    SettingsMenuItem(
                        text = text,
                        icon = icon,
                        textColor = if (text == "Delete Account") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenLightPreview() {
    AppTheme(darkTheme = false) {
        ProfileScreen(viewModel = ProfileViewModel(LocalProfileRepository(), RemoteProfileRepository()))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileScreen(viewModel = ProfileViewModel(LocalProfileRepository(), RemoteProfileRepository()))
    }
}