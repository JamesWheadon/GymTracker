package com.askein.gymtracker.ui.exercise.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.ExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistory
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import kotlinx.coroutines.launch

class RecordExerciseHistoryViewModel(
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository,
    private val cardioExerciseHistoryRepository: CardioExerciseHistoryRepository
) : ViewModel() {

    fun saveHistory(newHistory: ExerciseHistoryUiState) {
        viewModelScope.launch {
            when (newHistory) {
                is WeightsExerciseHistoryUiState -> weightsExerciseHistoryRepository.insert(
                    newHistory.toWeightsExerciseHistory()
                )

                is CardioExerciseHistoryUiState -> cardioExerciseHistoryRepository.insert(
                    newHistory.toCardioExerciseHistory()
                )
            }
        }
    }

    fun updateHistory(history: ExerciseHistoryUiState) {
        viewModelScope.launch {
            when (history) {
                is WeightsExerciseHistoryUiState -> weightsExerciseHistoryRepository.update(history.toWeightsExerciseHistory())
                is CardioExerciseHistoryUiState -> cardioExerciseHistoryRepository.update(history.toCardioExerciseHistory())
            }
        }
    }

    fun deleteHistory(history: ExerciseHistoryUiState) {
        viewModelScope.launch {
            when (history) {
                is WeightsExerciseHistoryUiState -> weightsExerciseHistoryRepository.delete(history.toWeightsExerciseHistory())
                is CardioExerciseHistoryUiState -> cardioExerciseHistoryRepository.delete(history.toCardioExerciseHistory())
            }
        }
    }
}
