package com.askein.gymtracker.ui.workout.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistory
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
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
    val savedWorkoutID: StateFlow<Int> = _savedWorkoutID.asStateFlow()

    fun saveWorkoutHistory(
        workoutHistory: WorkoutHistoryWithExercisesUiState,
        existingWorkoutHistory: WorkoutHistoryWithExercisesUiState,
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
                workoutHistory.exercises.forEach { exerciseHistory ->
                    when (exerciseHistory) {
                        is WeightsExerciseHistoryUiState -> {
                            if (exerciseHistory.id == 0) {
                                exerciseHistory.workoutHistoryId = workoutHistory.workoutHistoryId
                                weightsExerciseHistoryRepository.insert(exerciseHistory.toWeightsExerciseHistory())
                            } else {
                                weightsExerciseHistoryRepository.update(exerciseHistory.toWeightsExerciseHistory())
                            }
                        }
                        is CardioExerciseHistoryUiState -> {
                            if (exerciseHistory.id == 0) {
                                exerciseHistory.workoutHistoryId = workoutHistory.workoutHistoryId
                                cardioExerciseHistoryRepository.insert(exerciseHistory.toCardioExerciseHistory())
                            } else {
                                cardioExerciseHistoryRepository.update(exerciseHistory.toCardioExerciseHistory())
                            }
                        }
                    }
                }
                existingWorkoutHistory.exercises.forEach { exerciseHistory ->
                    if (!workoutHistory.exercises.map { it.exerciseId }.contains(exerciseHistory.exerciseId)) {
                        when (exerciseHistory) {
                            is WeightsExerciseHistoryUiState -> {
                                weightsExerciseHistoryRepository.delete(exerciseHistory.toWeightsExerciseHistory())
                            }
                            is CardioExerciseHistoryUiState -> {
                                cardioExerciseHistoryRepository.delete(exerciseHistory.toCardioExerciseHistory())
                            }
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

    fun liveDeleteWorkoutHistory() {
        viewModelScope.launch {
            weightsExerciseHistoryRepository.deleteAllForWorkoutHistory(savedWorkoutID.value)
            cardioExerciseHistoryRepository.deleteAllForWorkoutHistory(savedWorkoutID.value)
            workoutHistoryRepository.delete(savedWorkoutID.value)
        }
    }

    fun clearLiveWorkout() {
        viewModelScope.launch {
            _savedWorkoutID.emit(-1)
        }
    }
}
