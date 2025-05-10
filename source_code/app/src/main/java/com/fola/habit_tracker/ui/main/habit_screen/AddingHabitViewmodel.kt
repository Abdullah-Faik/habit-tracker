package com.fola.habit_tracker.ui.main.habit_screen

import android.app.Application
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fola.habit_tracker.HabitApplication
import com.fola.habit_tracker.R
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.database.RepeatedType
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class AddingHabitsViewmodel(private val repository: DataBaseHabitsRepository) : ViewModel() {

    private val _habit: MutableStateFlow<Habit> = MutableStateFlow<Habit>(Habit(title = ""))
    private val _isTitleError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isQuantityError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isUnitError: MutableStateFlow<Boolean> = MutableStateFlow(false)


    val habit = _habit.asStateFlow()
    val isTitleError = _isTitleError.asStateFlow()
    val isQuantityError = _isTitleError.asStateFlow()
    val isUnitError = _isTitleError.asStateFlow()


    fun setWholeHabit(habit: Habit) {
        _habit.value = habit
    }


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

    fun setHabitRepeat(repeatedType: RepeatedType, daysList: MutableSet<Int>) {
        _habit.value = _habit.value.copy(
            repeatedType = repeatedType,
            days = daysList
        )
    }

    fun setHabitTime(time: LocalTime) {
        _habit.value = _habit.value.copy(
            startTime = time
        )
    }

    fun setStartDate(date: LocalDate) {
        _habit.value = _habit.value.copy(
            startDate = date
        )
    }

    fun setEndDate(date: LocalDate) {
        Log.d("vdate", _habit.value.endDate.toString())
        _habit.value = _habit.value.copy(
            endDate = date
        )
        Log.d("vdate", _habit.value.endDate.toString())
    }

    fun setHabitTimesUnit(times: Int) {
        _habit.value = _habit.value.copy(
            timesOfUnit = times
        )
    }

    fun setUnit(unit: String) {
        _habit.value = _habit.value.copy(
            unit = unit
        )
    }

    fun setDay(day: Int) {
        val current = _habit.value.days
        val updated = if (day in current) current - day else current + day
        _habit.value = _habit.value.copy(days = updated.toMutableSet())
    }

    fun setNotification() {
        val newVal = _habit.value.notification.xor(1)
        _habit.value = _habit.value.copy(
            notification = newVal
        )
    }

    fun confirmationButton() {
        if (_habit.value.title.isBlank()) {
            _isTitleError.value = true
            return
        } else if (_habit.value.unit.isBlank()) {
            _isUnitError.value = true
            return
        } else if (_habit.value.timesOfUnit.equals(0)) {
            _isQuantityError.value = true
            return
        } else {
            viewModelScope.launch {
                repository.addNewHabit(
                    _habit.value
                )
                repository.addNewDailyHabit(
                    _habit.value
                )
                Log.d("habit", repository.getActiveHabit().toString())
            }
        }
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


fun provideAddingHabitsViewModelFactory(application: Application
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val app = application as HabitApplication
        val repository = app.habitsRepository
        AddingHabitsViewmodel(repository)
    }
}
