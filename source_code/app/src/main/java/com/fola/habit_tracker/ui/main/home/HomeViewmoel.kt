package com.fola.habit_tracker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fola.habit_tracker.HabitApplication
import com.fola.habit_tracker.data.data_base.Day
import com.fola.habit_tracker.data.data_base.DayWithHabits
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    private val _dailyHabits = MutableStateFlow(DayWithHabits(Day(LocalDate.now()), emptyList()))
    val habits: StateFlow<DayWithHabits> = _dailyHabits.asStateFlow()

    init {
        getDayHabit()
    }

    fun getDayHabit(dayId: LocalDate = LocalDate.now()) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.initNewDay(LocalDate.now())
            habitsRepository.getDailyHabits(dayId).collect { dailyHabits ->
                withContext(Dispatchers.Main) {
                    _dailyHabits.value = dailyHabits
                }
            }
        }
    }

    fun addNewHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.addNewDailyHabit(habit)
        }
    }

    fun removeDailyHabit(dayId: LocalDate, habitId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.deleteHabit(dayId, habitId)
        }
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

