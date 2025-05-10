package com.fola.habit_tracker.ui.main.timer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerViewModel : ViewModel() {
    private var _totalDuration = TimeUnit.MINUTES.toMillis(1)
    val totalDuration: Long
        get() = _totalDuration

    private val _timerState = MutableStateFlow(TimerState())
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        resetTimer()
    }

    fun setTotalDuration(durationMillis: Long) {
        _totalDuration = durationMillis.coerceAtLeast(0)
        resetTimer()
    }

    fun startTimer() {
        if (_timerState.value.isRunning || _timerState.value.remainingTime <= 0) return

        _timerState.value = _timerState.value.copy(isRunning = true)

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            var remaining = _timerState.value.remainingTime
            while (remaining > 0 && _timerState.value.isRunning) {
                kotlinx.coroutines.delay(1000) // Update every second
                if (_timerState.value.isRunning) {
                    remaining -= 1000
                    _timerState.value = _timerState.value.copy(
                        remainingTime = remaining.coerceAtLeast(0),
                        progress = (_totalDuration - remaining).toFloat() / _totalDuration
                    )
                }
            }
            if (remaining <= 0) {
                _timerState.value = _timerState.value.copy(
                    isRunning = false,
                    remainingTime = 0,
                    progress = 1f
                )
            }
        }
    }

    fun pauseTimer() {
        if (_timerState.value.isRunning) {
            _timerState.value = _timerState.value.copy(isRunning = false)
        }
    }

    fun resumeTimer() {
        if (!_timerState.value.isRunning && _timerState.value.remainingTime > 0) {
            startTimer()
        }
    }

    fun restartTimer() {
        stopTimer()
        resetTimer()
        startTimer()
    }

    fun stopTimer() {
        countdownJob?.cancel()
        _timerState.value = _timerState.value.copy(
            isRunning = false,
            remainingTime = 0,
            progress = 0f
        )
    }

    fun resetTimer() {
        countdownJob?.cancel()
        _timerState.value = TimerState(
            remainingTime = _totalDuration,
            progress = 0f,
            isRunning = false,
            totalDuration = _totalDuration
        )
    }

    data class TimerState(
        val remainingTime: Long = 0,
        val progress: Float = 0f,
        val isRunning: Boolean = false,
        val totalDuration: Long = 0
    )
}