package com.example.gymtracker.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val exerciseRepository: ExerciseRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val exerciseListUiState: StateFlow<ExerciseListUiState> =
        exerciseRepository.getAllExercisesStream().map { ExerciseListUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseListUiState()
            )

    fun getExerciseStream(id: Int): Flow<ExerciseUiState?> =
        exerciseRepository.getExerciseStream(id)
            .map { it?.toExerciseUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseUiState()
            )

    fun saveExercise(newExercise: Exercise) {
        viewModelScope.launch {
            exerciseRepository.insertExercise(newExercise)
        }
    }

    fun saveHistory(newHistory: ExerciseHistory) {
        viewModelScope.launch {
            historyRepository.insertHistory(newHistory)
        }
    }
}

data class ExerciseListUiState(val exerciseList: List<Exercise> = listOf())
