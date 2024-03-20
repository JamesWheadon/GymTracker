package com.askein.gymtracker.data

import android.content.Context
import com.askein.gymtracker.data.database.ExerciseWorkoutDatabase
import com.askein.gymtracker.data.exercise.ExerciseRepository
import com.askein.gymtracker.data.exercise.OfflineExerciseRepository
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.cardio.OfflineCardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.OfflineWeightsExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseWithHistory.ExerciseWithHistoryRepository
import com.askein.gymtracker.data.exerciseWithHistory.OfflineExerciseWithHistoryRepository
import com.askein.gymtracker.data.history.HistoryRepository
import com.askein.gymtracker.data.history.OfflineHistoryRepository
import com.askein.gymtracker.data.user.OfflineUserPreferencesRepository
import com.askein.gymtracker.data.user.UserPreferencesRepository
import com.askein.gymtracker.data.workout.OfflineWorkoutRepository
import com.askein.gymtracker.data.workout.WorkoutRepository
import com.askein.gymtracker.data.workoutExerciseCrossRef.OfflineWorkoutExerciseCrossRefRepository
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.data.workoutHistory.OfflineWorkoutHistoryRepository
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.askein.gymtracker.data.workoutWithExercises.OfflineWorkoutWithExercisesRepository
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercisesRepository

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
    val historyRepository: HistoryRepository
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
    override val historyRepository: HistoryRepository by lazy {
        OfflineHistoryRepository(ExerciseWorkoutDatabase.getDatabase(context).historyDao())
    }
}
