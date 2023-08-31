package com.example.gymtracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymtracker.GymTrackerApplication
import com.example.gymtracker.ui.exercise.ExerciseViewModel
import com.example.gymtracker.ui.exercise.details.ExerciseDetailsViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExerciseViewModel(
                gymTrackerApplication().container.exerciseRepository,
                gymTrackerApplication().container.historyRepository,
            )
        }
        initializer {
            ExerciseDetailsViewModel(
                gymTrackerApplication().container.exerciseRepository,
                gymTrackerApplication().container.historyRepository,
                this.createSavedStateHandle(),
            )
        }
    }
}

fun CreationExtras.gymTrackerApplication(): GymTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GymTrackerApplication)
