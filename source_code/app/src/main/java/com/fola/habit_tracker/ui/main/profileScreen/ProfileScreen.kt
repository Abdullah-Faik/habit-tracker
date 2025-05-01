package com.fola.habit_tracker.ui.main.profileScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.ui.theme.AppTheme

// ProfileScreen is the main composable function for the Profile screen UI.
// It displays the user's profile picture, name, notification settings, and a settings menu.
// Parameters:
// - modifier: Modifier to customize the layout's appearance or behavior (default is empty).
// - viewModel: ProfileViewModel to manage the screen's data (like user profile and notification settings).

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    // Collect the user profile data (name, image) and notification state from the ViewModel as State.
    val userProfile by viewModel.userProfile.collectAsState()
    val isNotificationsEnabled by viewModel.isNotificationsEnabled.collectAsState()

    // Scaffold provides a layout structure with a bottom bar (for navigation) and padding support.
    Scaffold() { paddingValues ->

        // Box is used as the main container for the screen content, with a gradient background.
        Box(
            modifier = modifier
                .fillMaxSize() // Fills the entire available space.
                .background(SolidColor(MaterialTheme.colorScheme.background))
                .padding(paddingValues) // Apply padding from Scaffold (to avoid overlapping with the bottom bar).
                .padding(top = 48.dp), // Extra padding at the top for better spacing.
            contentAlignment = Alignment.TopCenter // Center the content at the top.
        ) {
            // Column arranges the profile picture, name, notifications row, and settings menu vertically.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally.
            ) {
                // Box to hold the profile picture and the edit button.
                Box(
                    modifier = Modifier
                        .size(160.dp) // Fixed size for the profile picture container.
                ) {
                    // Image displays the user's profile picture.
                    Image(
                        painter = painterResource(id = userProfile.profileImageRes), // Load the image from the resource ID in the userProfile data.
                        contentDescription = "Profile picture", // Accessibility description.
                        contentScale = ContentScale.Crop, // Crop the image to fit the circular shape.
                        modifier = Modifier
                            .fillMaxSize() // Fill the entire Box.
                            .clip(CircleShape) // Clip the image into a circular shape.
                            .border(
                                width = 4.dp, // Add a border around the image.
                                brush = Brush.linearGradient( // Gradient border using primary and secondary colors.
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shadow(8.dp, CircleShape) // Add a shadow for a 3D effect.
                    )
                    // FloatingActionButton for editing the profile, positioned at the bottom-right of the image.
                    FloatingActionButton(
                        onClick = { viewModel.editProfile() }, // Call the editProfile function in the ViewModel (to navigate to edit screen).
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Align at the bottom-right corner of the Box.
                            .size(40.dp), // Fixed size for the button.
                        containerColor = MaterialTheme.colorScheme.primary, // Background color of the button.
                        contentColor = MaterialTheme.colorScheme.onPrimary // Color of the icon inside the button.
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit, // Edit icon.
                            contentDescription = "Edit profile" // Accessibility description.
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp)) // Add vertical spacing between the image and the name.

                // Text displays the user's name.
                Text(
                    text = userProfile.name, // User's name from the ViewModel.
                    color = MaterialTheme.colorScheme.onBackground, // Text color suitable for the background.
                    fontSize = 28.sp, // Large font size for the name.
                    fontWeight = FontWeight.ExtraBold, // Bold text for emphasis.
                    modifier = Modifier.shadow(
                        2.dp,
                        ambientColor = MaterialTheme.colorScheme.primary
                    ) // Add a shadow with the primary color.
                )

                Spacer(modifier = Modifier.height(24.dp)) // Add vertical spacing between the name and the notifications row.

                // NotificationsRow displays the toggle for enabling/disabling notifications.
                NotificationsRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // Horizontal padding for better spacing.
                        .fillMaxWidth(), // Fill the available width.
                    isNotificationsEnabled = isNotificationsEnabled, // Current state of notifications (from ViewModel).
                    onToggleNotifications = { viewModel.toggleNotifications(it) } // Callback to toggle notifications in the ViewModel.
                )

                Spacer(modifier = Modifier.height(24.dp)) // Add vertical spacing between the notifications row and the settings menu.

                // SettingsMenu displays a list of settings options (like Change Password, Logout, etc.).
                SettingsMenu(
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // Horizontal padding for better spacing.
                        .fillMaxWidth(), // Fill the available width.
                    viewModel = viewModel // Pass the ViewModel to handle settings actions.
                )
            }
        }
    }
}

// NotificationsRow is a composable function that displays a row for toggling notifications.
// It shows a card with a notifications icon, label, and a switch to enable/disable notifications.
// Parameters:
// - modifier: Modifier to customize the layout's appearance or behavior (default is empty).
// - isNotificationsEnabled: Boolean to indicate if notifications are enabled (from ViewModel).
// - onToggleNotifications: Callback function to handle toggling the notifications state.

@Composable
fun NotificationsRow(
    modifier: Modifier = Modifier,
    isNotificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit
) {

    // Animate the scale of the card when notifications are enabled/disabled (for a subtle effect).
    val scale by animateFloatAsState(
        targetValue = if (isNotificationsEnabled) 1.05f else 1f, // Slightly scale up when enabled.
        animationSpec = tween(200) // Animation duration of 200ms.
    )
    // Animate the card's background color based on the notification state.
    val cardColor by animateColorAsState(
        targetValue = if (isNotificationsEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, // Use secondary color when enabled, surfaceVariant when disabled.
        animationSpec = tween(200) // Animation duration of 200ms.
    )

    // Card to wrap the row content, providing a background and shadow.
    Card(
        modifier = modifier
            .scale(scale) // Apply the animated scale effect.
            .shadow(4.dp, RoundedCornerShape(16.dp)), // Add a shadow with rounded corners.
        colors = CardDefaults.cardColors(containerColor = cardColor) // Set the card's background color (animated).
    ) {
        // Row to arrange the icon, text, and switch horizontally.
        Row(
            modifier = Modifier
                .fillMaxWidth() // Fill the available width.
                .padding(horizontal = 16.dp, vertical = 12.dp), // Padding inside the card.
            verticalAlignment = Alignment.CenterVertically // Center items vertically.
        ) {
            // Icon to display the notifications symbol.
            Icon(
                imageVector = Icons.Default.Notifications, // Notifications icon.
                contentDescription = "Toggle notifications", // Accessibility description.
                tint = MaterialTheme.colorScheme.onSecondary // Color suitable for the card's background.
            )
            Spacer(modifier = Modifier.width(12.dp)) // Add horizontal spacing between the icon and the text.

            // Text to display the "Notifications" label.
            Text(
                text = "Notifications", // Label text.
                color = MaterialTheme.colorScheme.onSecondary, // Text color suitable for the card's background.
                fontSize = 16.sp, // Medium font size.
                fontWeight = FontWeight.Medium, // Medium weight for emphasis.
                modifier = Modifier.weight(1f) // Take up remaining space to push the switch to the right.
            )

            // Switch to toggle notifications on/off.
            Switch(
                checked = isNotificationsEnabled, // Current state of the switch (from ViewModel).
                onCheckedChange = onToggleNotifications, // Callback to handle toggle changes.
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary, // Color of the thumb when checked.
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), // Color of the track when checked (semi-transparent).
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurface, // Color of the thumb when unchecked.
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) // Color of the track when unchecked (semi-transparent).
                )
            )
        }
    }
}

// SettingsMenuItem is a composable function that displays a single settings option in the menu.
// It shows an icon, a label, and responds to clicks with a slight scale animation for feedback.
// Parameters:
// - text: The label of the settings option (e.g., "Change Password").
// - icon: The ImageVector icon to display next to the label.
// - onClick: Callback function to handle clicks on the item.
// - modifier: Modifier to customize the layout's appearance or behavior (default is empty).
// - textColor: Color of the text (default is onSurface from the theme).
@Composable
fun SettingsMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    // Create an interaction source to detect press events (for animation).
    val interactionSource = remember { MutableInteractionSource() }
    // Track if the item is being pressed.
    val isPressed by interactionSource.collectIsPressedAsState()
    // Animate the scale of the item when pressed (for a subtle click effect).
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f, // Scale down slightly when pressed.
        animationSpec = tween(100) // Animation duration of 100ms.
    )

    // Row to arrange the icon and text horizontally.
    Row(
        modifier = modifier
            .fillMaxWidth() // Fill the available width.
            .scale(scale) // Apply the animated scale effect.
            .clickable( // Make the row clickable.
                interactionSource = interactionSource, // Use the interaction source for press detection.
                indication = null, // Disable default ripple effect (we're using scale animation instead).
                onClick = onClick // Call the onClick callback when clicked.
            )
            .padding(horizontal = 16.dp, vertical = 18.dp), // Padding inside the row.
        verticalAlignment = Alignment.CenterVertically // Center items vertically.
    ) {
        // Icon to display the settings option symbol.
        Icon(
            imageVector = icon, // Icon passed as a parameter (e.g., Lock for "Change Password").
            contentDescription = text, // Accessibility description (same as the label).
            tint = textColor, // Icon color (same as the text color).
            modifier = Modifier.size(24.dp) // Fixed size for the icon.
        )
        Spacer(modifier = Modifier.width(12.dp)) // Add horizontal spacing between the icon and the text.

        // Text to display the settings option label.
        Text(
            text = text, // Label passed as a parameter.
            color = textColor, // Text color (default is onSurface).
            fontSize = 16.sp, // Medium font size.
            fontWeight = FontWeight.Medium // Medium weight for emphasis.
        )
    }
}

// SettingsMenu is a composable function that displays a list of settings options in a card.
// It includes options like "Change Password", "Reset Data", "Logout", and "Delete Account".
// Parameters:
// - modifier: Modifier to customize the layout's appearance or behavior (default is empty).
// - viewModel: ProfileViewModel to handle actions for each settings option.
@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    // Card to wrap the settings options, providing a background and shadow.
    Card(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(16.dp)), // Add a shadow with rounded corners.
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Set the card's background color.
    ) {
        // Column to arrange the settings options vertically.
        Column {
            // First option: "Change Password".
            SettingsMenuItem(
                text = "Change Password", // Label for the option.
                icon = Icons.Default.Lock, // Lock icon to represent password change.
                textColor = MaterialTheme.colorScheme.onSurface, // Text/icon color.
                onClick = { viewModel.changePassword() } // Call the changePassword function in the ViewModel.
            )
            // Divider to separate options.
            HorizontalDivider(
                thickness = 1.dp, // Thickness of the divider.
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) // Semi-transparent divider color.
            )
            // Second option: "Reset Data".
            SettingsMenuItem(
                text = "Reset Data", // Label for the option.
                icon = Icons.Default.Warning, // Warning icon to indicate a potentially dangerous action.
                textColor = MaterialTheme.colorScheme.onSurface, // Text/icon color.
                onClick = { viewModel.resetData() } // Call the resetData function in the ViewModel.
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            // Third option: "Logout".
            SettingsMenuItem(
                text = "Logout", // Label for the option.
                icon = Icons.Default.Person, // Person icon to represent user logout.
                textColor = MaterialTheme.colorScheme.onSurface, // Text/icon color.
                onClick = { viewModel.logout() } // Call the logout function in the ViewModel.
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            // Fourth option: "Delete Account".
            SettingsMenuItem(
                text = "Delete Account", // Label for the option.
                icon = Icons.Default.Warning, // Warning icon to indicate a dangerous action.
                onClick = { viewModel.deleteAccount() } // Call the deleteAccount function in the ViewModel.
            )
        }
    }
}


// ProfileScreenLightPreview is a composable function for previewing the ProfileScreen in Light Mode.
// It wraps the ProfileScreen in AppTheme with darkTheme set to false to simulate Light Mode.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenLightPreview() {
    // Wrap the screen in AppTheme to apply the Light Mode theme.
    AppTheme(darkTheme = false) {
        // Create a NavController for navigation (used in preview).
        val navController = rememberNavController()
        // Create a ProfileRepository and ProfileViewModel for preview data.
        val repository = ProfileRepository()
        val viewModel = ProfileViewModel(repository)
        // Render the ProfileScreen with the NavController and ViewModel.
        ProfileScreen(
            viewModel = viewModel
        )
    }
}

// ProfileScreenDarkPreview is a composable function for previewing the ProfileScreen in Dark Mode.
// It wraps the ProfileScreen in AppTheme with darkTheme set to true to simulate Dark Mode.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenDarkPreview() {
    // Wrap the screen in AppTheme to apply the Dark Mode theme.
    AppTheme(darkTheme = true) {
        // Create a NavController for navigation (used in preview).
        val navController = rememberNavController()
        // Create a ProfileRepository and ProfileViewModel for preview data.
        val repository = ProfileRepository()
        val viewModel = ProfileViewModel(repository)
        // Render the ProfileScreen with the NavController and ViewModel.
        ProfileScreen(
            viewModel = viewModel
        )
    }
}