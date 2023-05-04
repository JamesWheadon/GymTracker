package com.example.gymtracker.data

import android.content.Context
import com.example.gymtracker.data.exercise.ExerciseDatabase
import com.example.gymtracker.data.exercise.ExercisesRepository
import com.example.gymtracker.data.exercise.OfflineExercisesRepository
import com.example.gymtracker.data.history.HistoryDatabase
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.data.history.OfflineHistoryRepository

interface AppContainer {
    val exercisesRepository: ExercisesRepository
    val historyRepository: HistoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exercisesRepository: ExercisesRepository by lazy {
        OfflineExercisesRepository(ExerciseDatabase.getDatabase(context).exerciseDao())
    }
    override val historyRepository: HistoryRepository by lazy {
        OfflineHistoryRepository(HistoryDatabase.getDatabase(context).historyDao())
    }
}
