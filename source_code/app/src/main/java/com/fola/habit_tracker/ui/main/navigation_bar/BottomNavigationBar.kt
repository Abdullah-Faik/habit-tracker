package com.fola.habit_tracker.ui.main.navigation_bar

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.theme.AppTheme

enum class Screen(
    val route: String,
    @DrawableRes val icon: Int,
    val title: String,
) {
    Home("home", R.drawable.home_icon, "Home"),
    Habit("habit", R.drawable.tasks, "Habit"),
    Timer("timer", R.drawable.timer_icon, "Timer"),
    Profile("profile", R.drawable.profile_icon, "Profile")
}


private val bottomNavigationScreens = listOf(
    Screen.Home,
    Screen.Habit,
    Screen.Timer,

    Screen.Profile
)

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(40, 40, 0, 0))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        bottomNavigationScreens.forEach { screen ->
            BottomNavigationItemView(
                modifier = modifier.weight(1f),
                screen = screen,
                navController = navController,
                isSelected = currentRoute == screen.route,
            )
        }
    }
}

@Composable
fun BottomNavigationItemView(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    navController: NavController,
    screen: Screen,
) {

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(screen.icon),
            contentDescription = screen.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .background(
                    brush = SolidColor(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color(0x00000000)
                    ),
                    shape = RoundedCornerShape(30),
                    alpha = .30f
                )
                .clickable {
                    if (navController.currentDestination?.route != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                .padding(vertical = 2.dp)
                .height(28.dp)
        )
    }
}


@Preview
@Composable
private fun BottomNavigationBarPrev() {
    AppTheme {
        BottomNavigationBar(
            navController = rememberNavController()
        )
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavigationDarkView() {
    AppTheme {
        BottomNavigationBar(navController = rememberNavController())
    }
}