package com.example.gymtracker.ui.workout.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    private val workoutHistoryRepository: WorkoutHistoryRepository,
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository
) : ViewModel() {

    private val _savedWorkoutID = MutableStateFlow(-1)
    private val savedWorkoutID: StateFlow<Int> = _savedWorkoutID.asStateFlow()

    fun saveWorkoutHistory(
        workoutHistory: WorkoutHistory,
        workoutExercises: List<WeightsExerciseHistory>,
        save: Boolean = true
    ) {
        viewModelScope.launch {
            if (save) {
                val workoutHistoryId = workoutHistoryRepository.insert(workoutHistory).toInt()
                workoutExercises.map { exerciseHistory ->
                    exerciseHistory.workoutHistoryId = workoutHistoryId
                    exerciseHistory
                }.forEach { exerciseHistory ->
                    weightsExerciseHistoryRepository.insertHistory(exerciseHistory)
                }
            } else {
                workoutHistoryRepository.update(workoutHistory)
                workoutExercises.forEach { exerciseHistory ->
                    weightsExerciseHistoryRepository.update(exerciseHistory)
                }
            }
        }
    }

    fun saveWorkoutHistory(
        workoutHistory: WorkoutHistory
    ) {
        viewModelScope.launch {
            Log.i("WorkoutHistoryViewModel", workoutHistory.toString())
            val savedID = workoutHistoryRepository.insert(workoutHistory).toInt()
            _savedWorkoutID.emit(savedID)
        }
    }

    fun saveWorkoutExercise(
        workoutExercise: WeightsExerciseHistory
    ) {
        viewModelScope.launch {
            workoutExercise.workoutHistoryId = savedWorkoutID.value
            Log.i("WorkoutHistoryViewModel", workoutExercise.toString())
            weightsExerciseHistoryRepository.insertHistory(workoutExercise)
        }
    }

    fun deleteWorkoutHistory(
        workoutHistory: WorkoutHistory,
        workoutExercises: List<WeightsExerciseHistory>
    ) {
        viewModelScope.launch {
            workoutHistoryRepository.delete(workoutHistory)
            workoutExercises.forEach { exerciseHistory ->
                weightsExerciseHistoryRepository.delete(exerciseHistory)
            }
        }
    }
}
