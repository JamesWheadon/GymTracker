package com.example.gymtracker.data

import android.content.Context
import com.example.gymtracker.data.database.ExerciseDatabase
import com.example.gymtracker.data.database.HistoryDatabase
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exercise.OfflineExerciseRepository
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.data.history.OfflineHistoryRepository
import com.example.gymtracker.data.workout.OfflineWorkoutRepository
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.OfflineWorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutWithExercises.OfflineWorkoutWithExercisesRepository
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository

interface AppContainer {
    val exerciseRepository: ExerciseRepository
    val historyRepository: HistoryRepository
    val workoutRepository: WorkoutRepository
    val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
    val workoutWithExerciseRepository: WorkoutWithExercisesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exerciseRepository: ExerciseRepository by lazy {
        OfflineExerciseRepository(ExerciseDatabase.getDatabase(context).exerciseDao())
    }
    override val historyRepository: HistoryRepository by lazy {
        OfflineHistoryRepository(HistoryDatabase.getDatabase(context).historyDao())
    }
    override val workoutRepository: WorkoutRepository by lazy {
        OfflineWorkoutRepository(ExerciseDatabase.getDatabase(context).workoutDao())
    }
    override val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository by lazy {
        OfflineWorkoutExerciseCrossRefRepository(ExerciseDatabase.getDatabase(context).workoutExerciseCrossRefDao())
    }
    override val workoutWithExerciseRepository: WorkoutWithExercisesRepository by lazy {
        OfflineWorkoutWithExercisesRepository(ExerciseDatabase.getDatabase(context).workoutWithExercisesDao())
    }
}
