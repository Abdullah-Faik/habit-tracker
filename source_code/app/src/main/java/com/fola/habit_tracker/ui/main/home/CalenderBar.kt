package com.fola.habit_tracker.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun DateRow(
    modifier: Modifier = Modifier,
    onCardPress: (LocalDate) -> Unit = {},
    selectedDay : LocalDate = LocalDate.now()
) {

    val listState = rememberLazyListState()
    val today = LocalDate.now()
    val todayIndex = today.dayOfMonth - 1
    val monthLength = today.lengthOfMonth()
    val dates = (1..monthLength).map { day -> today.withDayOfMonth(day) }


    LaunchedEffect(Unit) {
        listState.scrollToItem((todayIndex - 2).coerceAtLeast(0))
    }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(dates) { date ->
            DateDayCard(
                dayNumber = date.dayOfMonth.toString(),
                dayName = date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.UK
                ),
                isCurrentDay = date == today,
                onCardPress = {
                    onCardPress(date)
                },
                isSelected = date == selectedDay

            )
        }
    }
}

@Composable
fun DateDayCard(
    modifier: Modifier = Modifier,
    dayNumber: String = "",
    dayName: String = "",
    isCurrentDay: Boolean = false,
    onCardPress: () -> Unit = {},
    isSelected: Boolean = false
) {
    Box(
        Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onCardPress() }
            .border(
                width = if (isSelected) (1.5).dp else (-1).dp,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            .background(if (isCurrentDay) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow)
            .defaultMinSize(minWidth = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                dayNumber,
                color = if (isCurrentDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
            Text(
                text = dayName,
                color = if (isCurrentDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun DataRowPrev() {
    AppTheme {
        DateRow()
    }
}

@Preview
@Composable
private fun DateCardPrev() {
    DateDayCard()
}