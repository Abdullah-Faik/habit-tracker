package com.fola.habit_tracker.ui.main.habit_screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.main.navigation_bar.HabitSubRoutes
import com.fola.habit_tracker.ui.main.navigation_bar.Screen
import com.fola.habit_tracker.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    navController: NavController,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val resolvedStartDes = currentBackStackEntry?.arguments?.getString("startDes") ?: HabitSubRoutes.MAIN
    val resolvedRoute = Screen.Habit.createHabitRoute(resolvedStartDes)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Text(
                        text = "Habits",
                        fontWeight = FontWeight.Bold,
                        fontFamily = interFont,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {}
            )
        },
        floatingActionButton = {
            if (resolvedRoute == Screen.Habit.createHabitRoute(HabitSubRoutes.MAIN)) {
                Icon(
                    imageVector = PlusSmall,
                    contentDescription = "Add habit",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .size(56.dp)
                        .clickable {
                            Log.d("HabitScreen", "Navigating to habit/adding_habit")
                            navController.navigate(
                                Screen.Habit.createHabitRoute(HabitSubRoutes.ADDING_HABIT)
                            )
                        }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Log.d("current", currentRoute.toString())
            when (resolvedRoute) {
                Screen.Habit.createHabitRoute(HabitSubRoutes.MAIN) -> {
                    Log.d("HabitScreen", "Rendering HabitMainContent")
                    HabitMainContent()
                }
                Screen.Habit.createHabitRoute(HabitSubRoutes.ADDING_HABIT) -> {
                    Log.d("HabitScreen", "Rendering AddingHabitScreen")
                    AddingHabitScreen(navController)
                }
                else -> {
                    Log.d("HabitScreen", "Fallback to HabitMainContent")
                    HabitMainContent()
                }
            }
        }
    }
}

@Composable
fun HabitMainContent() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier,
        ) {

            Spacer(modifier = Modifier.size(200.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.checklist),
                    contentDescription = "no habit added",
                    modifier = Modifier
                        .size(150.dp)
                        .alpha(0.6f),
                    alignment = Alignment.Center,
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No habits",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .alpha(0.6f)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "There is no upcoming habits",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .alpha(0.6f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightTasksPrev() {
    AppTheme {
        HabitScreen(navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkTasksPrev() {
    AppTheme {
        HabitScreen(navController = rememberNavController())
    }
}