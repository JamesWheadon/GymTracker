package com.example.gymtracker.ui.workout.history.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import kotlinx.coroutines.launch

class RecordWorkoutHistoryViewModel(
    private val workoutHistoryRepository: WorkoutHistoryRepository,
    private val exerciseHistoryRepository: ExerciseHistoryRepository
) : ViewModel() {

    fun saveWorkoutHistory(
        workoutHistory: WorkoutHistory,
        workoutExercises: List<ExerciseHistory>
    ) {
        viewModelScope.launch {
            val workoutHistoryId = workoutHistoryRepository.insert(workoutHistory).toInt()
            workoutExercises.map { exerciseHistory ->
                exerciseHistory.workoutHistoryId = workoutHistoryId
                exerciseHistory
            }.forEach { exerciseHistory ->
                exerciseHistoryRepository.insertHistory(exerciseHistory)
            }
        }
    }
}