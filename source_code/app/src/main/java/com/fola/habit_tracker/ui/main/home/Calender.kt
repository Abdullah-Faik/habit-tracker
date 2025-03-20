package com.fola.habit_tracker.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun DateRow(modifier: Modifier = Modifier) {

    val listState = rememberLazyListState()
    val today = LocalDate.now()
    val todayIndex = today.dayOfMonth - 1
    val monthLength = today.lengthOfMonth()
    val dates = (1..monthLength).map { day -> today.withDayOfMonth(day) }


    LaunchedEffect(Unit) {
        listState.scrollToItem(todayIndex)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(dates) { date ->
            DateDayCard(
                dayNumber = date.dayOfMonth.toString(),
                dayName = date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.getDefault()
                ),
                isCurrentDay = date == today

            )
        }
    }
}

@Composable
fun DateDayCard(
    modifier: Modifier = Modifier,
    dayNumber: String = "",
    dayName: String = "",
    isCurrentDay: Boolean = false
) {
    Box(
        Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isCurrentDay) (1.5).dp else (-1).dp,
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.main_color)
            )
            .background(color = colorResource(R.color.secondary_background_color))
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
                color = colorResource(R.color.main_color),
                fontSize = 20.sp
            )
            Text(
                text = dayName,
                color = if (isCurrentDay) colorResource(R.color.main_color) else colorResource(R.color.white),
                fontSize = 14.sp
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun DataRowPrev() {
    DateRow()
}

@Composable
private fun DateCardPrev() {
    DateDayCard()
}