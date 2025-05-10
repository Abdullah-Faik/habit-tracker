package com.fola.habit_tracker.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.components.mainFont
import com.fola.habit_tracker.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProfileCard(modifier: Modifier = Modifier) {

    Row(
        modifier = Modifier
            .padding(
                horizontal = 8.dp,
                vertical = 24.dp
            )
            .clip(RoundedCornerShape(16))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        var showCalendar by remember { mutableStateOf(false) }
        if (showCalendar) {
            CalenderPicker(
                onConfirm = { showCalendar = false },
                onDismiss = { showCalendar = false },
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "avatar",
                Modifier
                    .border(4.dp, Color.White, RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .size(98.dp),
                contentScale = ContentScale.Crop,

                )
            Column {
                Text(
                    text = "HI, Name \uD83D\uDC4B\uD83C\uDFFB",
                    fontWeight = FontWeight.Bold,
                    fontFamily = mainFont,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
                Text(LocalDate.now().format(
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
                ))
            }
        }
        IconButton(
            onClick = {
                showCalendar = !showCalendar
            },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .size(52.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "calender",
                Modifier.size(36.dp)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewCard() {
    AppTheme {
        ProfileCard()

    }
}


    
