package com.example.gymtracker.ui.workout.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toCardioExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    private val workoutHistoryRepository: WorkoutHistoryRepository,
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository,
    private val cardioExerciseHistoryRepository: CardioExerciseHistoryRepository
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

    fun liveSaveWorkoutHistory(
        workoutHistory: WorkoutHistory
    ) {
        viewModelScope.launch {
            val savedID = workoutHistoryRepository.insert(workoutHistory).toInt()
            _savedWorkoutID.emit(savedID)
        }
    }

    fun liveSaveWorkoutExerciseHistory(
        workoutExercise: ExerciseHistoryUiState
    ) {
        viewModelScope.launch {
            when (workoutExercise) {
                is WeightsExerciseHistoryUiState -> {
                    workoutExercise.workoutId = savedWorkoutID.value
                    weightsExerciseHistoryRepository.insertHistory(workoutExercise.toWeightsExerciseHistory())
                }
                is CardioExerciseHistoryUiState -> {
                    workoutExercise.workoutId = savedWorkoutID.value
                    cardioExerciseHistoryRepository.insert(workoutExercise.toCardioExerciseHistory())
                }
            }
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
