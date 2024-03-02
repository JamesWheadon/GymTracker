package com.example.gymtracker.ui.workout.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
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
        workoutHistory: WorkoutHistoryWithExercisesUiState,
        save: Boolean = true
    ) {
        viewModelScope.launch {
            if (save) {
                val workoutHistoryId = workoutHistoryRepository.insert(workoutHistory.toWorkoutHistoryUiState().toWorkoutHistory()).toInt()
                workoutHistory.exercises.forEach { exerciseHistory ->
                    when (exerciseHistory) {
                        is WeightsExerciseHistoryUiState -> {
                            exerciseHistory.workoutHistoryId = workoutHistoryId
                            weightsExerciseHistoryRepository.insert(exerciseHistory.toWeightsExerciseHistory())
                        }
                        is CardioExerciseHistoryUiState -> {
                            exerciseHistory.workoutHistoryId = workoutHistoryId
                            cardioExerciseHistoryRepository.insert(exerciseHistory.toCardioExerciseHistory())
                        }
                    }
                }
            } else {
                workoutHistoryRepository.update(workoutHistory.toWorkoutHistoryUiState().toWorkoutHistory())
                workoutHistory.exercises.forEach { exerciseHistory ->
                    when (exerciseHistory) {
                        is WeightsExerciseHistoryUiState -> {
                            weightsExerciseHistoryRepository.update(exerciseHistory.toWeightsExerciseHistory())
                        }
                        is CardioExerciseHistoryUiState -> {
                            cardioExerciseHistoryRepository.update(exerciseHistory.toCardioExerciseHistory())
                        }
                    }
                }
            }
        }
    }

    fun liveSaveWorkoutHistory(
        workoutHistory: WorkoutHistoryUiState
    ) {
        viewModelScope.launch {
            val savedID = workoutHistoryRepository.insert(workoutHistory.toWorkoutHistory()).toInt()
            _savedWorkoutID.emit(savedID)
        }
    }

    fun liveSaveWorkoutExerciseHistory(
        workoutExercise: ExerciseHistoryUiState
    ) {
        viewModelScope.launch {
            when (workoutExercise) {
                is WeightsExerciseHistoryUiState -> {
                    workoutExercise.workoutHistoryId = savedWorkoutID.value
                    weightsExerciseHistoryRepository.insert(workoutExercise.toWeightsExerciseHistory())
                }
                is CardioExerciseHistoryUiState -> {
                    workoutExercise.workoutHistoryId = savedWorkoutID.value
                    cardioExerciseHistoryRepository.insert(workoutExercise.toCardioExerciseHistory())
                }
            }
        }
    }

    fun deleteWorkoutHistory(
        workoutHistory: WorkoutHistoryWithExercisesUiState
    ) {
        viewModelScope.launch {
            workoutHistoryRepository.delete(workoutHistory.toWorkoutHistoryUiState().toWorkoutHistory())
            workoutHistory.exercises.forEach { workoutExercise ->
                when (workoutExercise) {
                    is WeightsExerciseHistoryUiState -> {
                        weightsExerciseHistoryRepository.delete(workoutExercise.toWeightsExerciseHistory())
                    }
                    is CardioExerciseHistoryUiState -> {
                        cardioExerciseHistoryRepository.delete(workoutExercise.toCardioExerciseHistory())
                    }
                }
            }
        }
    }
}
