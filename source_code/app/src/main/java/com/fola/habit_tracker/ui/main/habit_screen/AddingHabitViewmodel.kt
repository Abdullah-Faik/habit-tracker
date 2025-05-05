package com.fola.habit_tracker.ui.main.habit_screen

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.data_base.RepeatedType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalTime

class AddingHabitsViewmodel(/*private val habitsRepository: HabitsRepository*/) : ViewModel() {

    private val _habit: MutableStateFlow<Habit> = MutableStateFlow<Habit>(Habit(title = ""))
    val habit = _habit.asStateFlow()


    fun setHabitTitle(title: String) {
        _habit.value = _habit.value.copy(title = title)
    }


    fun setHabitDescription(description: String) {
        _habit.value = _habit.value.copy(description = description)
    }

    fun setHabitColor(color: Long, name: String) {
        _habit.value = _habit.value.copy(
            color = color,
            colorName = name
        )
    }

    fun setHabitIcon(icon: Int, description: String) {
        _habit.value = _habit.value.copy(
            icon = icon,
            iconDescription = description
        )
    }

    fun setHabitRepeat(repeatedType: RepeatedType, daysList: List<Int>) {
        _habit.value = _habit.value.copy(
            repeatedType = repeatedType,
            daya = daysList
        )
    }

    fun setHabitTime(time : LocalTime) {
        _habit.value = _habit.value.copy(
            startTime = time
        )
    }

    fun setStartDate(date : LocalDate) {
        _habit.value = _habit.value.copy(
            startDate =  date
        )
    }

    fun setEndDate(date: LocalDate) {
        _habit.value = _habit.value.copy(
            endDate = date
        )
    }

    fun setHabitTime(times : Int) {
        _habit.value = _habit.value.copy(
            timesOfUnit = times
        )
    }

    fun setUnit(unit : String) {
        _habit.value = _habit.value.copy(
            unit = unit
        )
    }





}


data class HabitColor(val color: Long, val name: String)

val habitColors = listOf(
    HabitColor(0xFF00BCD5, "Cyan"),
    HabitColor(0xFF03A9F5, "Light Blue"),
    HabitColor(0xFF2196F3, "Blue"),
    HabitColor(0xFF4052B6, "Indigo"),
    HabitColor(0xFF683AB7, "Deep Purple"),
    HabitColor(0xFF9D27B1, "Purple"),
    HabitColor(0xFF607D8D, "Blue Grey"),
    HabitColor(0xFF9E9E9E, "Grey"),
    HabitColor(0xFF795549, "Brown"),
    HabitColor(0xFFF44236, "Red"),
    HabitColor(0xFFFE5722, "Deep Orange"),
    HabitColor(0xFFFF9702, "Orange"),
    HabitColor(0xFFFEC009, "Amber"),
    HabitColor(0xFFFFEB3C, "Yellow"),
    HabitColor(0xFFC9DA3A, "Lime"),
    HabitColor(0xFF4CB050, "Green"),
    HabitColor(0xFF009788, "Teal")
)


data class HabitIcon(
    @DrawableRes val icon: Int,
    val name: String
)

val habitIcons = listOf(
    HabitIcon(R.drawable.programming, "Coding"),
    HabitIcon(R.drawable.eating, "Eating"),
    HabitIcon(R.drawable.running, "Sport")
)


//fun provideAddingHabitsViewModelFactory(
//    application: Application
//): ViewModelProvider.Factory = viewModelFactory {
//    initializer {
//        val app = application as HabitApplication
//        val repository = app.habitsRepository
//        AddingHabitsViewmodel(repository)
//    }
//}
