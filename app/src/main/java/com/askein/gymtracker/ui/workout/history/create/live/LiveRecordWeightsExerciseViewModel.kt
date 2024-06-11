package com.askein.gymtracker.ui.workout.history.create.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class LiveRecordWeightsExerciseViewModel : ViewModel() {

    val exerciseState = MutableStateFlow(WeightsExerciseHistoryUiState())
    val timerState = MutableStateFlow(TimerState())
    val completed = MutableStateFlow(false)
    val unitState = MutableStateFlow(WeightUnits.KILOGRAMS)
    private var timerJob: Job? = null

    fun setExerciseData(exerciseId: Int, rest: Int) {
        exerciseState.tryEmit(
            WeightsExerciseHistoryUiState(
                exerciseId = exerciseId,
                rest = rest,
            )
        )
    }

    fun setUnitState(units: WeightUnits) {
        unitState.tryEmit(units)
    }

    fun addSetInfo(reps: Int, weight: Double) {
        val oldState = exerciseState.value
        val newState = oldState.copy(reps = oldState.reps + reps, weight = oldState.weight + weight)
        exerciseState.tryEmit(newState)
    }

    fun finishSet() {
        exerciseState.tryEmit(exerciseState.value.copy(sets = exerciseState.value.sets + 1))
    }

    fun startTimer(rest: Int) {
        timerJob = viewModelScope.launch {
            timerState.tryEmit(timerState.value.copy(currentTime = rest, timerRunning = true, endTime = Instant.now().plusSeconds(rest.toLong() + 1).truncatedTo(ChronoUnit.SECONDS)))
            while (timerState.value.currentTime > 0) {
                withContext(Dispatchers.Main) {
                    timerState.tryEmit(timerState.value.copy(currentTime = ChronoUnit.SECONDS.between(Instant.now(), timerState.value.endTime).toInt()))
                }
                delay(1000)
            }
            timerState.tryEmit(timerState.value.copy(timerRunning = false))
            completed.tryEmit(true)
        }
    }

    fun reset() {
        timerJob?.cancel()
        timerState.tryEmit(timerState.value.copy(timerRunning = false))
        completed.tryEmit(false)
    }
}
