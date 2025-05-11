package com.fola.habit_tracker.ui.main.home

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.fola.habit_tracker.ui.auth.LoginScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HomeViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    private val _dayWithHabits = MutableStateFlow(DayWithHabits(Day(LocalDate.now()), emptyList()))
    val habits: StateFlow<DayWithHabits> = _dayWithHabits.asStateFlow()


    private val _dailyHabits = MutableStateFlow(DailyHabits())
    val dailyHabits: StateFlow<DailyHabits> = _dailyHabits.asStateFlow()

    private val _day = MutableStateFlow(Day())
    val day: StateFlow<Day> = _day.asStateFlow()


    init {
        viewModelScope.launch {
            if (habitsRepository.getDay(LocalDate.now()) == null)
                habitsRepository.initNewDay(LocalDate.now())
            _day.collect { selectedDay ->
                getDayHabit(selectedDay.dayId)
            }
        }
    }

    fun getDayHabit(dayId: LocalDate = LocalDate.now()) {
        viewModelScope.launch(Dispatchers.IO) {


            habitsRepository.getDailyHabits(dayId).collect { dailyHabits ->
                withContext(Dispatchers.Main) {
                    _dayWithHabits.value = dailyHabits
                }
            }
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


    fun getDailyHabitProgress(dayId: LocalDate = _day.value.dayId, habitId: Long): Flow<Int> {
        return habitsRepository.getDailyHabitProgress(dayId, habitId)
    }


    fun setCurrentDate(localDate: LocalDate = LocalDate.now()) {
        _day.value = Day(localDate)
    }

    fun getDayProgress(dayId: LocalDate = _day.value.dayId): Flow<Float> {
        return habitsRepository.getDayProgress(dayId)
    }


    fun getCompletedHabit(): Flow<Int> = _dayWithHabits.flatMapLatest { d ->
        combine(
            d.habits.map { habit ->
                habitsRepository.getDailyHabitProgress(_day.value.dayId, habit.id)
                    .map { progress -> progress to habit.timesOfUnit }
            }
        ) { completions ->
            completions.count { (p, z) -> p >= z }
        }
    }

    fun increase(h: Habit) {
        viewModelScope.launch(Dispatchers.IO) {

            var cProgress = habitsRepository.getDailyHabitProgressInt(LocalDate.now(), h.id)
            if (_day.value.dayId == LocalDate.now() &&
                cProgress < h.timesOfUnit
            ) {
                cProgress += 1
                habitsRepository.updateProgress(dayId = LocalDate.now(), habitId = h.id, cProgress)
            }
        }

    }

    fun deCrease(h: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            var cProgress = habitsRepository.getDailyHabitProgressInt(LocalDate.now(), h.id)
            if (_day.value.dayId == LocalDate.now() &&
                cProgress > 0
            ) {
                cProgress -= 1
                habitsRepository.updateProgress(dayId = LocalDate.now(), habitId = h.id, cProgress)
            }
        }
    }

    fun markDone(h: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.updateProgress(dayId = LocalDate.now(), habitId = h.id, h.timesOfUnit)
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

