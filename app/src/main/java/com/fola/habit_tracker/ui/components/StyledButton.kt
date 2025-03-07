package com.fola.habit_tracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.R

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = ""
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.main_color)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontFamily = interFont,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
        )
    }


}