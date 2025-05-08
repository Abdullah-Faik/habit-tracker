package com.fola.habit_tracker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fola.habit_tracker.HabitApplication
import com.fola.habit_tracker.data.database.DailyHabits
import com.fola.habit_tracker.data.database.Day
import com.fola.habit_tracker.data.database.DayWithHabits
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    private val _dayWithHabits = MutableStateFlow(DayWithHabits(Day(LocalDate.now()), emptyList()))
    val habits: StateFlow<DayWithHabits> = _dayWithHabits.asStateFlow()

    private val _dailyHabits = MutableStateFlow(DailyHabits())
    val dailyHabits: StateFlow<DailyHabits> = _dailyHabits.asStateFlow()

    private val _day = MutableStateFlow(Day(LocalDate.now()))
    val day: StateFlow<Day> = _day.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _day.collect {
                getDayHabit(it.dayId)
            }
        }
    }

    fun getDayHabit(dayId: LocalDate = _day.value.dayId) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.initNewDay(LocalDate.now())
            habitsRepository.getDailyHabits(dayId).collect { dailyHabits ->
                withContext(Dispatchers.Main) {
                    _dayWithHabits.value = dailyHabits
                }
            }
        }
    }

    fun setCurrentDay(dayId: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _day.value = Day(dayId)
        }
    }


    fun addNewHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.addNewHabit(habit)
            habitsRepository.addNewDailyHabit(habit)
        }
    }

    fun removeDailyHabit(dayId: LocalDate, habitId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.deleteHabit(dayId, habitId)
        }
    }

    fun getDayProgress(dayId: LocalDate): Flow<Float> {
        return habitsRepository.getDayProgress(dayId)
    }


    fun getDailyHabitProgress(dayId: LocalDate, habitId: Long): Flow<Float> {
        return habitsRepository.getDailyHabitProgress(dayId, habitId)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as HabitApplication)
                val repository: DataBaseHabitsRepository = application.habitsRepository
                HomeViewModel(repository)
            }
        }
    }
}

