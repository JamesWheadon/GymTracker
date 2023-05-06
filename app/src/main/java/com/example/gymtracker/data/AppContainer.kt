package com.example.gymtracker.data

import android.content.Context
import com.example.gymtracker.data.exercise.ExerciseDatabase
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exercise.OfflineExerciseRepository
import com.example.gymtracker.data.history.HistoryDatabase
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.data.history.OfflineHistoryRepository

interface AppContainer {
    val exerciseRepository: ExerciseRepository
    val historyRepository: HistoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exerciseRepository: ExerciseRepository by lazy {
        OfflineExerciseRepository(ExerciseDatabase.getDatabase(context).exerciseDao())
    }
    override val historyRepository: HistoryRepository by lazy {
        OfflineHistoryRepository(HistoryDatabase.getDatabase(context).historyDao())
    }
}
