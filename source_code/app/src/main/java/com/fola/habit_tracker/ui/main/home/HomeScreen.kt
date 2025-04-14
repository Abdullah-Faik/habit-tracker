package com.fola.habit_tracker.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R
import com.fola.habit_tracker.ui.components.mainFont
import com.fola.habit_tracker.ui.theme.AppTheme


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp,
                        vertical = 24.dp)
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
                    onClick = {},
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .size(52.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "calender",
                        Modifier.size(36.dp)
                    )
                }
            }
            DateRow()
            ProgressCard(modifier = Modifier.padding(horizontal = 12.dp))
        }


    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomePrev() {
    AppTheme {
        HomeScreen()
    }
}
@Preview(showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeDarkPrev() {
    AppTheme {
        HomeScreen()
    }
}