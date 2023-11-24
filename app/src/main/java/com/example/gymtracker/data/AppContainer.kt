package com.example.gymtracker.data

import android.content.Context
import com.example.gymtracker.data.database.ExerciseWorkoutDatabase
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exercise.OfflineExerciseRepository
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.OfflineExerciseHistoryRepository
import com.example.gymtracker.data.workout.OfflineWorkoutRepository
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.OfflineWorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.data.workoutHistory.OfflineWorkoutHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.data.workoutWithExercises.OfflineWorkoutWithExercisesRepository
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository

interface AppContainer {
    val exerciseRepository: ExerciseRepository
    val exerciseHistoryRepository: ExerciseHistoryRepository
    val workoutRepository: WorkoutRepository
    val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
    val workoutWithExerciseRepository: WorkoutWithExercisesRepository
    val workoutHistoryRepository: WorkoutHistoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exerciseRepository: ExerciseRepository by lazy {
        OfflineExerciseRepository(ExerciseWorkoutDatabase.getDatabase(context).exerciseDao())
    }
    override val exerciseHistoryRepository: ExerciseHistoryRepository by lazy {
        OfflineExerciseHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).exerciseHistoryDao())
    }
    override val workoutRepository: WorkoutRepository by lazy {
        OfflineWorkoutRepository(ExerciseWorkoutDatabase.getDatabase(context).workoutDao())
    }
    override val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository by lazy {
        OfflineWorkoutExerciseCrossRefRepository(ExerciseWorkoutDatabase.getDatabase(context).workoutExerciseCrossRefDao())
    }
    override val workoutWithExerciseRepository: WorkoutWithExercisesRepository by lazy {
        OfflineWorkoutWithExercisesRepository(ExerciseWorkoutDatabase.getDatabase(context).workoutWithExercisesDao())
    }
    override val workoutHistoryRepository: WorkoutHistoryRepository by lazy {
        OfflineWorkoutHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).workoutHistoryDao())
    }
}
