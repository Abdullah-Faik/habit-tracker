package com.fola.habit_tracker.ui.profileScreen

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

data class UserProfile(
    val name: String,
    @DrawableRes val profileImageRes: Int
)

