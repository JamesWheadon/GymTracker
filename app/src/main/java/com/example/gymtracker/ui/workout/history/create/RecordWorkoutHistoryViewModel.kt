package com.example.gymtracker.ui.workout.history.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryWithExerciseHistory
import kotlinx.coroutines.launch

class RecordWorkoutHistoryViewModel(
    private val workoutHistoryRepository: WorkoutHistoryRepository
) : ViewModel() {

    fun saveWorkout(workoutHistory: WorkoutHistoryWithExerciseHistory) {
        viewModelScope.launch {
            workoutHistoryRepository.insert(workoutHistory)
        }
    }
}