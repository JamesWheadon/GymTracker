package com.example.gymtracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymtracker.GymTrackerApplication
import com.example.gymtracker.ui.exercise.ExercisesScreenViewModel
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsViewModel
import com.example.gymtracker.ui.history.RecordHistoryViewModel
import com.example.gymtracker.ui.workout.WorkoutScreenViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExercisesScreenViewModel(
                gymTrackerApplication().container.exerciseRepository
            )
        }
        initializer {
            ExerciseDetailsViewModel(
                gymTrackerApplication().container.exerciseRepository,
                gymTrackerApplication().container.historyRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            RecordHistoryViewModel(
                gymTrackerApplication().container.historyRepository
            )
        }
        initializer {
            WorkoutScreenViewModel(
                gymTrackerApplication().container.workoutRepository
            )
        }
    }
}

fun CreationExtras.gymTrackerApplication(): GymTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymTrackerApplication)
