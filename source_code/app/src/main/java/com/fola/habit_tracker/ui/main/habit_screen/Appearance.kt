package com.fola.habit_tracker.ui.main.habit_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties


@Composable
fun CustomDivider() {
    Spacer(modifier = Modifier.height(24.dp))

    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 2.dp,
        color = Color(0x33333232)
    )

    Spacer(modifier = Modifier.height(24.dp))

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropdown(
    modifier: Modifier = Modifier,
    selectedColor: Long = habitColors.first().color,
    colorName: String = habitColors.first().name,
    onColorSelected: (Long, String) -> Unit,
    colorOptions: List<HabitColor>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Trigger Button
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true }
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .background(brush = SolidColor(Color(selectedColor)), alpha = 0.14f)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(selectedColor))
            )
            Spacer(Modifier.width(8.dp))
            Text(colorName)
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)


        }

        // Dropdown
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = rememberScrollState(0),
            modifier = Modifier.heightIn(max = 256.dp),
            properties = PopupProperties(focusable = false),

            ) {
            colorOptions.forEach { color ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(color.color))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(color.name)
                        }
                    },
                    onClick = {
                        onColorSelected(color.color, color.name)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconsDropMenu(
    modifier: Modifier = Modifier,
    icon: Int = habitIcons.first().icon,
    description: String = habitIcons.first().name,
    onSelectedIcon: (Int, String) -> Unit,
    iconsList: List<HabitIcon>,

    ) {

    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { expanded = true }
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = description,
                modifier = Modifier
                    .size(40.dp),
                contentScale = ContentScale.Inside
            )
            Spacer(Modifier.width(8.dp))
            Text(description)
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)

        }

        // Dropdown
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = rememberScrollState(0),
            modifier = Modifier.heightIn(max = 256.dp),
            properties = PopupProperties(focusable = false),

            ) {
            iconsList.forEach { icon ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(icon.icon),
                                contentDescription = icon.name,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(icon.name)
                        }
                    },
                    onClick = {
                        onSelectedIcon(icon.icon, icon.name)
                        expanded = false
                    }
                )
            }
        }
    }
}