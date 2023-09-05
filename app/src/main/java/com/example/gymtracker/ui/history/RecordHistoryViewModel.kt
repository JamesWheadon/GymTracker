package com.example.gymtracker.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordHistoryViewModel(
    exerciseRepository: ExerciseRepository,
    private val historyRepository: HistoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseId: Int = checkNotNull(savedStateHandle["exerciseId"])

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val exerciseUiState: StateFlow<ExerciseUiState> =
        exerciseRepository.getExerciseStream(exerciseId)
            .map { exercise -> exercise?.toExerciseUiState() ?: ExerciseUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExerciseUiState()
            )

    fun saveHistory(newHistory: ExerciseHistory) {
        viewModelScope.launch {
            historyRepository.insertHistory(newHistory)
        }
    }
}