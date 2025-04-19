package com.fola.habit_tracker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fola.habit_tracker.HabitApplication
import com.fola.habit_tracker.data.data_base.Habit
import com.fola.habit_tracker.data.repositry.DataBaseHabitsRepository
import com.fola.habit_tracker.data.repositry.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class HomeViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()


    init {
        getHabits()
    }

    private fun getHabits() {
        viewModelScope.launch {
            habitsRepository.getAllHabits().collect {
                _habits.value = it
            }
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

