package com.example.gymtracker.ui.exercise.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
import kotlinx.coroutines.launch

class RecordExerciseHistoryViewModel(
    private val exerciseHistoryRepository: ExerciseHistoryRepository
) : ViewModel() {

    fun saveHistory(newHistory: ExerciseHistory) {
        viewModelScope.launch {
            exerciseHistoryRepository.insertHistory(newHistory)
        }
    }

    fun updateHistory(history: ExerciseHistory) {
        viewModelScope.launch {
            exerciseHistoryRepository.update(history)
        }
    }

    fun deleteHistory(history: ExerciseHistory) {
        viewModelScope.launch {
            exerciseHistoryRepository.delete(history)
        }
    }
}
