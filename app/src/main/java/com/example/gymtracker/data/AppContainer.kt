package com.example.gymtracker.data

import android.content.Context

interface AppContainer {
    val exercisesRepository: ExercisesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exercisesRepository: ExercisesRepository by lazy {
        OfflineExercisesRepository(ExerciseDatabase.getDatabase(context).exerciseDao())
    }
}
