package com.fola.habit_tracker.ui.main.habit_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fola.habit_tracker.data.database.Habit
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AllHabits(private val repository: DataBaseHabitsRepository) : ViewModel() {

    private var _habit: MutableStateFlow<List<Habit>> = MutableStateFlow<List<Habit>>(emptyList())
    val habit = _habit.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getActiveHabitsFlow().collect {
                _habit.value = it
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHabit(LocalDate.now(), habitId = habit.id)
        }
    }
    fun notify(habit : Habit) {
        viewModelScope.launch {
            repository.setNotify(habit.id , habit.notification.xor(1))
        }
    }

}