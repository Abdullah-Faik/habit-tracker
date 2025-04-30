package com.fola.habit_tracker.ui.main.home

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.mainFont
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate


@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val habits = viewModel.habits.collectAsState()
    var showCalendar by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            Icon(
                imageVector = PlusSmall,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .size(56.dp)
                    .clickable {
                        viewModel.addNewHabit(Habit(title = "test"))
                    },
            )

        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (showCalendar) {
                CalenderPicker(
                    onConfirm = { showCalendar = false },
                    onDismiss = { showCalendar = false },
                )
            }
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "avatar",
                        Modifier
                            .size(98.dp)
                    )
                    Text(
                        text = "HI, Name \uD83D\uDC4B\uD83C\uDFFB",
                        fontWeight = FontWeight.Bold,
                        fontFamily = mainFont,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                }
                IconButton(
                    onClick = {
                        Log.d("calendar", "clicked")
                        showCalendar = !showCalendar
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .size(52.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "ca`lender",
                        Modifier.size(36.dp)
                    )
                }
            }
            DateRow(
                modifier = Modifier
                    .padding(vertical = 4.dp),
                onCardPress = {
                    Log.d("card", "day is $it")
                    viewModel.getDayHabit(it)
                }
            )
            ProgressCard(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                items(items = habits.value.habits, key = { it.id }) { habit ->
                    TasksCard(
                        modifier = Modifier.padding(vertical = 2.dp),
                        habit = habit,
                        progress = 0.3f,
                        onClickable = {
                            Log.d("clicking", "clicked")
                            viewModel.removeDailyHabit(LocalDate.now(), habit.id)
                        }
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            viewModel = HomeViewModel(FakeHabitsRepository())
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {
    AppTheme {
        HomeScreen(
            viewModel = HomeViewModel(FakeHabitsRepository())
        )
    }
}