package com.example.gymtracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymtracker.GymTrackerApplication
import com.example.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsViewModel
import com.example.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import com.example.gymtracker.ui.workout.WorkoutScreenViewModel
import com.example.gymtracker.ui.workout.details.WorkoutDetailsViewModel
import com.example.gymtracker.ui.workout.details.WorkoutExerciseCrossRefViewModel
import com.example.gymtracker.ui.workout.history.WorkoutHistoryViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExercisesScreenViewModel(
                exerciseRepository = gymTrackerApplication().container.exerciseRepository
            )
        }
        initializer {
            ExerciseDetailsViewModel(
                exerciseRepository = gymTrackerApplication().container.exerciseRepository,
                weightsExerciseHistoryRepository = gymTrackerApplication().container.weightsExerciseHistoryRepository,
                workoutExerciseCrossRefRepository = gymTrackerApplication().container.workoutExerciseCrossRefRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            RecordExerciseHistoryViewModel(
                weightsExerciseHistoryRepository = gymTrackerApplication().container.weightsExerciseHistoryRepository
            )
        }
        initializer {
            WorkoutScreenViewModel(
                workoutRepository = gymTrackerApplication().container.workoutRepository
            )
        }
        initializer {
            WorkoutDetailsViewModel(
                workoutRepository = gymTrackerApplication().container.workoutRepository,
                workoutWithExercisesRepository = gymTrackerApplication().container.workoutWithExerciseRepository,
                workoutExerciseCrossRefRepository = gymTrackerApplication().container.workoutExerciseCrossRefRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            WorkoutExerciseCrossRefViewModel(
                gymTrackerApplication().container.workoutExerciseCrossRefRepository
            )
        }
        initializer {
            WorkoutHistoryViewModel(
                gymTrackerApplication().container.workoutHistoryRepository,
                gymTrackerApplication().container.weightsExerciseHistoryRepository
            )
        }
    }
}

fun CreationExtras.gymTrackerApplication(): GymTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymTrackerApplication)
