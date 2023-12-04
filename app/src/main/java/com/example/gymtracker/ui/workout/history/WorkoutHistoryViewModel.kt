package com.example.gymtracker.ui.workout.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import kotlinx.coroutines.launch

class WorkoutHistoryViewModel(
    private val workoutHistoryRepository: WorkoutHistoryRepository,
    private val exerciseHistoryRepository: ExerciseHistoryRepository
) : ViewModel() {

    fun saveWorkoutHistory(
        workoutHistory: WorkoutHistory,
        workoutExercises: List<ExerciseHistory>,
        save: Boolean = true
    ) {
        viewModelScope.launch {
            if (save) {
                val workoutHistoryId = workoutHistoryRepository.insert(workoutHistory).toInt()
                workoutExercises.map { exerciseHistory ->
                    exerciseHistory.workoutHistoryId = workoutHistoryId
                    exerciseHistory
                }.forEach { exerciseHistory ->
                    exerciseHistoryRepository.insertHistory(exerciseHistory)
                }
            } else {
                workoutHistoryRepository.update(workoutHistory)
                workoutExercises.forEach { exerciseHistory ->
                    exerciseHistoryRepository.update(exerciseHistory)
                }
            }
        }
    }

    fun deleteWorkoutHistory(
        workoutHistory: WorkoutHistory,
        workoutExercises: List<ExerciseHistory>
    ) {
        viewModelScope.launch {
            workoutHistoryRepository.delete(workoutHistory)
            workoutExercises.forEach { exerciseHistory ->
                exerciseHistoryRepository.delete(exerciseHistory)
            }
        }
    }
}
