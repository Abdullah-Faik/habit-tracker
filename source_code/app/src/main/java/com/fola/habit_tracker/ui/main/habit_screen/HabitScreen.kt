package com.fola.habit_tracker.ui.main.habit_screen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.task_screen.TaskNavigation
import com.fola.habit_tracker.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(navController: NavController) {
    val applicationContext = LocalContext.current

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
                navigationIcon = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                var selectedTab by remember { mutableStateOf(0) } // 0 = Single, 1 = Recurring

                Column(
                    modifier = Modifier,
                ) {
                    // Tab Row (Single / Recurring)
                    TabRow(
                        selectedTabIndex = selectedTab,
                        indicator = { tabPositions ->
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTab])
                                    .padding(horizontal = 67.dp)
                                    .fillMaxWidth(0.5f)
                                    .height(2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                            )
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    text = "Single Habits",
                                    fontFamily = interFont,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                            unselectedContentColor = Color(0xff353434)
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    text = "Recurring Habits",
                                    fontFamily = interFont,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            selectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                            unselectedContentColor = Color(0xff353434)
                        )
                    }
                }

                Column(
                    modifier = Modifier
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
            val context = LocalContext.current

            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                onClick = {
                    try { // CHANGED: Added try-catch to handle navigation errors
                        navController.navigate("addingHabit") // Navigate to adding task
                    } catch (e: IllegalArgumentException) { // CHANGED: Log error to prevent crash
                        Log.e("NavigationError", "Failed to navigate to addingHabit: ${e.message}")
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.addfab),
                    contentDescription = "Large floating action button",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LightTasksPrev() {
    AppTheme {
        TaskNavigation()// CHANGED: Use TaskNavigation to ensure NavController is linked to NavHost
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DarkTasksPrev() {
    AppTheme {
        TaskNavigation() // CHANGED: Use TaskNavigation to ensure NavController is linked to NavHost
    }
}