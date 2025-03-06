package com.fola.habit_tracker.screens.ui_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R


val interFont = FontFamily(
    Font(R.font.inter_18pt_regular, FontWeight.Normal),
    Font(R.font.inter_18pt_black, FontWeight.Black),
    Font(R.font.inter_24pt_extra_bold, FontWeight.ExtraBold)
)

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.basic_background))
            .padding(vertical = 100.dp, horizontal = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
        ) {
            Image(
                painter = painterResource(R.drawable.yoga_girl),
                contentDescription = stringResource(R.string.yoga_girl)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Habit".uppercase(),
                color = Color.White,
                fontSize = 64.sp,
                fontFamily = interFont,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Unleash your potential through movement",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontFamily = interFont
            )
        }

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.main_color)
            )
        ) {
            Text(
                text = "Lets Go",
                color = Color.White,
                fontFamily = interFont,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                modifier = Modifier.padding(12.dp)
            )

        }

    }

}


@Preview
@Composable
private fun WelcomePrev() {
    WelcomeScreen()
}