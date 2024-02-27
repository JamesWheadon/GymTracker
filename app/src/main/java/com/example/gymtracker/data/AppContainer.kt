package com.example.gymtracker.data

import android.content.Context
import com.example.gymtracker.data.database.ExerciseWorkoutDatabase
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exercise.OfflineExerciseRepository
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.cardio.OfflineCardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.OfflineWeightsExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.exerciseWithHistory.ExerciseWithHistoryRepository
import com.example.gymtracker.data.exerciseWithHistory.OfflineExerciseWithHistoryRepository
import com.example.gymtracker.data.user.OfflineUserPreferencesRepository
import com.example.gymtracker.data.user.UserPreferencesRepository
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
    val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository
    val cardioExerciseHistoryRepository: CardioExerciseHistoryRepository
    val exerciseWithHistoryRepository: ExerciseWithHistoryRepository
    val workoutRepository: WorkoutRepository
    val workoutExerciseCrossRefRepository: WorkoutExerciseCrossRefRepository
    val workoutWithExerciseRepository: WorkoutWithExercisesRepository
    val workoutHistoryRepository: WorkoutHistoryRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val exerciseRepository: ExerciseRepository by lazy {
        OfflineExerciseRepository(ExerciseWorkoutDatabase.getDatabase(context).exerciseDao())
    }
    override val weightsExerciseHistoryRepository: WeightsExerciseHistoryRepository by lazy {
        OfflineWeightsExerciseHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).weightsExerciseHistoryDao())
    }
    override val cardioExerciseHistoryRepository: CardioExerciseHistoryRepository by lazy {
        OfflineCardioExerciseHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).cardioExerciseHistoryDao())
    }
    override val exerciseWithHistoryRepository: ExerciseWithHistoryRepository by lazy {
        OfflineExerciseWithHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).exerciseWithHistoryDao())
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
    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        OfflineUserPreferencesRepository(context)
    }
}
