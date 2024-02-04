package com.example.gymtracker.ui.exercise.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.exercise.history.ExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.toExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.toExerciseDetailsUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseDetailsViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository,
    private val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: Int = checkNotNull(savedStateHandle["exerciseId"])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<ExerciseDetailsUiState> =
        exerciseRepository.getExerciseStream(exerciseId)
            .map { exercise -> exercise?.toExerciseDetailsUiState() ?: ExerciseDetailsUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseDetailsUiState()
            )

    val exerciseHistory: StateFlow<List<ExerciseHistoryUiState>> =
        weightsExerciseHistoryRepository.getFullExerciseHistoryStream(exerciseId)
        .map { historyList -> historyList?.map { history -> history.toExerciseHistoryUiState() } ?: listOf() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.updateExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            weightsExerciseHistoryRepository.deleteAllForExercise(exercise)
            workoutExerciseCrossRefRepository.deleteAllCrossRefForExercise(exercise)
            exerciseRepository.deleteExercise(exercise)
        }
    }
}
