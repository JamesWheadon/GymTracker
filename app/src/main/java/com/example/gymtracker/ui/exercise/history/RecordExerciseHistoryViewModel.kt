package com.example.gymtracker.ui.exercise.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import kotlinx.coroutines.launch

class RecordExerciseHistoryViewModel(
    private val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository
) : ViewModel() {

    fun saveHistory(newHistory: WeightsExerciseHistory) {
        viewModelScope.launch {
            weightsExerciseHistoryRepository.insertHistory(newHistory)
        }
    }

    fun updateHistory(history: WeightsExerciseHistory) {
        viewModelScope.launch {
            weightsExerciseHistoryRepository.update(history)
        }
    }

    fun deleteHistory(history: WeightsExerciseHistory) {
        viewModelScope.launch {
            weightsExerciseHistoryRepository.delete(history)
        }
    }
}
