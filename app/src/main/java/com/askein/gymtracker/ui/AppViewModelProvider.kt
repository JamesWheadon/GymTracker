package com.askein.gymtracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.askein.gymtracker.GymTrackerApplication
import com.askein.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.askein.gymtracker.ui.exercise.details.ExerciseDetailsViewModel
import com.askein.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import com.askein.gymtracker.ui.history.OverallHistoryViewModel
import com.askein.gymtracker.ui.user.UserPreferencesViewModel
import com.askein.gymtracker.ui.workout.WorkoutScreenViewModel
import com.askein.gymtracker.ui.workout.details.WorkoutDetailsViewModel
import com.askein.gymtracker.ui.workout.details.WorkoutExerciseCrossRefViewModel
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryViewModel

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
                cardioExerciseHistoryRepository = gymTrackerApplication().container.cardioExerciseHistoryRepository,
                workoutExerciseCrossRefRepository = gymTrackerApplication().container.workoutExerciseCrossRefRepository,
                exerciseWithHistoryRepository = gymTrackerApplication().container.exerciseWithHistoryRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            RecordExerciseHistoryViewModel(
                weightsExerciseHistoryRepository = gymTrackerApplication().container.weightsExerciseHistoryRepository,
                cardioExerciseHistoryRepository = gymTrackerApplication().container.cardioExerciseHistoryRepository
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
                gymTrackerApplication().container.weightsExerciseHistoryRepository,
                gymTrackerApplication().container.cardioExerciseHistoryRepository
            )
        }
        initializer {
            UserPreferencesViewModel(
                gymTrackerApplication().container.userPreferencesRepository
            )
        }
        initializer {
            OverallHistoryViewModel(
                gymTrackerApplication().container.historyRepository
            )
        }
    }
}

fun CreationExtras.gymTrackerApplication(): GymTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymTrackerApplication)