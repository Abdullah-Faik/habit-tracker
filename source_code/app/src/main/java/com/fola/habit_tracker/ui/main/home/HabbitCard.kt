package com.fola.habit_tracker.ui.main.home

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.ui.components.icons.PlusSmall
import com.fola.habit_tracker.ui.components.icons.minus
import com.fola.habit_tracker.ui.components.interFont
import com.fola.habit_tracker.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun TasksCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    onClickable: () -> Unit = {},
    progress: Flow<Int>,
    onIncrease: (Habit) -> Unit,
    onDecrease: (Habit) -> Unit,
    onDone: (Habit) -> Unit,
) {
    val prog = progress.collectAsState(0).value
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(600f, 600f)
    )
    var isTapped by remember { mutableStateOf(false) }
    var isLongPressed by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(if (isTapped) 0.97f else 1f, animationSpec = tween(150))
    val progressScale by animateFloatAsState(
        if (prog > 0 && prog == habit.timesOfUnit) 1.15f else 1f,
        animationSpec = tween(400)
    )
    val haptic = LocalHapticFeedback.current
    var showTooltip by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(showTooltip) {
        if (showTooltip != null) {
            delay(800)
            showTooltip = null
        }
    }

    LaunchedEffect(prog) {
        if (prog > 0) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            .scale(cardScale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isTapped = true
                        onClickable()
                        isTapped = false
                    },
                    onLongPress = {
                        isLongPressed = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (prog == habit.timesOfUnit) {
                            repeat(prog) { onDecrease(habit) }
                            showTooltip = "Habit undone"
                        } else {
                            repeat(habit.timesOfUnit - prog) { onIncrease(habit) }
                            onDone(habit)
                            showTooltip = "Habit completed!"
                        }
                        isLongPressed = false
                    }
                )
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .background(gradientBrush)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(habit.icon),
                        contentDescription = habit.iconDescription,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(5.dp)
                            .aspectRatio(1f),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Spacer(Modifier.padding(horizontal = 6.dp))
                    Column {
                        Text(
                            text = habit.title,
                            fontSize = 18.sp,
                            fontFamily = interFont,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.semantics { contentDescription = "Habit title: ${habit.title}" }
                        )
                        Spacer(Modifier.padding(vertical = 1.dp))
                        Text(
                            text = "$prog ${habit.unit} of ${habit.timesOfUnit}",
                            fontSize = 12.sp,
                            fontFamily = interFont,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.semantics { contentDescription = "Progress: $prog of ${habit.timesOfUnit} ${habit.unit} completed" }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .scale(progressScale)
                        .shadow(4.dp, CircleShape, ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { (prog.toFloat() / habit.timesOfUnit).coerceIn(0f, 1f) },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text(
                        text = "${((prog.toFloat() / habit.timesOfUnit) * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.semantics { contentDescription = "Progress percentage: ${((prog.toFloat() / habit.timesOfUnit) * 100).toInt()} percent" }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HabitActionButton(
                    onClickable = {
                        onDecrease(habit)
                        showTooltip = "Decreased"
                    },
                    backgroundColor = MaterialTheme.colorScheme.error,
                    icon = minus,
                    contentDescription = "Decrease progress by one ${habit.unit}"
                )

                var sliderValue by remember { mutableFloatStateOf(prog.toFloat()) }
                LaunchedEffect(prog) {
                    sliderValue = prog.toFloat()
                }

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onValueChangeFinished = {
                        val newProg = sliderValue.roundToInt()
                        when {
                            newProg > prog -> repeat(newProg - prog) { onIncrease(habit) }
                            newProg < prog -> repeat(prog - newProg) { onDecrease(habit) }
                        }
                        if (newProg == habit.timesOfUnit) {
                            onDone(habit)
                            showTooltip = "Completed!"
                        } else {
                            showTooltip = "Set to $newProg ${habit.unit}"
                        }
                    },
                    valueRange = 0f..habit.timesOfUnit.toFloat(),
                    steps = habit.timesOfUnit - 1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                        .semantics { contentDescription = "Adjust progress for ${habit.title}" },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                )

                HabitActionButton(
                    onClickable = {
                        onIncrease(habit)
                        showTooltip = "Increased"
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    icon = PlusSmall,
                    contentDescription = "Increase progress by one ${habit.unit}"
                )
            }

            if (showTooltip != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = showTooltip!!,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .semantics { contentDescription = "Action feedback: $showTooltip" }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification time for ${habit.title}",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.padding(horizontal = 3.dp))
                Text(
                    text = "${habit.repeatedType.name.lowercase().replaceFirstChar { it.uppercase() }} at ${
                        habit.startTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                    }",
                    fontSize = 10.sp,
                    fontFamily = interFont,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.semantics { contentDescription = "Reminder: ${habit.repeatedType.name} at ${habit.startTime.format(DateTimeFormatter.ofPattern("h:mm a"))}" }
                )
            }
        }
    }
}

@Composable
fun HabitActionButton(
    modifier: Modifier = Modifier,
    onClickable: () -> Unit = {},
    backgroundColor: Color,
    icon: ImageVector,
    contentDescription: String?,
    isActive: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.85f else 1f, animationSpec = tween(150))
    val animatedColor by animateColorAsState(
        if (isPressed) backgroundColor.copy(alpha = 0.8f) else backgroundColor,
        animationSpec = tween(150)
    )
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(animatedColor)
            .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
            .clickable(enabled = isActive) {
                isPressed = true
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClickable()
                isPressed = false
            }
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

private val habit = Habit(
    title = "Drink Water",
    unit = "glass",
    timesOfUnit = 8
)

@Preview(showBackground = true)
@Composable
private fun TasksCardLightPrev() {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = Modifier.padding(16.dp),
            onClickable = {},
            progress = flow { emit(4) },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun TasksCardDarkPrev() {
    AppTheme {
        TasksCard(
            habit = habit,
            modifier = Modifier.padding(16.dp),
            onClickable = {},
            progress = flow { emit(6) },
            onDone = {},
            onDecrease = {},
            onIncrease = {}
        )
    }
}