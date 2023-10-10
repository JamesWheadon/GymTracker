package com.example.gymtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryRepository
import kotlinx.coroutines.launch

class RecordHistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    fun saveHistory(newHistory: ExerciseHistory) {
        viewModelScope.launch {
            historyRepository.insertHistory(newHistory)
        }
    }

    fun updateHistory(history: ExerciseHistory) {
        viewModelScope.launch {
            historyRepository.update(history)
        }
    }
}