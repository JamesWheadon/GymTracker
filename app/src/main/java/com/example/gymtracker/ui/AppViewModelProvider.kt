package com.example.gymtracker.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymtracker.ExerciseApplication
import com.example.gymtracker.ui.exercise.ExerciseViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExerciseViewModel(
                ExerciseApplication().container.exerciseRepository,
                ExerciseApplication().container.historyRepository
            )
        }
    }
}
