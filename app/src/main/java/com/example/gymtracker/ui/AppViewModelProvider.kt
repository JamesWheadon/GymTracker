package com.example.gymtracker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymtracker.ExerciseApplication
import com.example.gymtracker.ui.exercise.ExerciseViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ExerciseViewModel(
                ExerciseApplication().container.exerciseRepository
            )
        }
    }
}

fun CreationExtras.exerciseApplication(): ExerciseApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ExerciseApplication)
