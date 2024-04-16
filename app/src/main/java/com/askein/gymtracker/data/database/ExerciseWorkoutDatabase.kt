package com.askein.gymtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseDao
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryDao
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryDao
import com.askein.gymtracker.data.exerciseWithHistory.ExerciseWithHistoryDao
import com.askein.gymtracker.data.history.HistoryDao
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workout.WorkoutDao
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefDao
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryDao
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercisesDao
import com.askein.gymtracker.util.LocalDateConverter

@Database(
    entities = [
        Exercise::class,
        WeightsExerciseHistory::class,
        CardioExerciseHistory::class,
        Workout::class,
        WorkoutExerciseCrossRef::class,
        WorkoutHistory::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class ExerciseWorkoutDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun weightsExerciseHistoryDao(): WeightsExerciseHistoryDao
    abstract fun cardioExerciseHistoryDao(): CardioExerciseHistoryDao
    abstract fun exerciseWithHistoryDao(): ExerciseWithHistoryDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseCrossRefDao(): WorkoutExerciseCrossRefDao
    abstract fun workoutWithExercisesDao(): WorkoutWithExercisesDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var Instance: ExerciseWorkoutDatabase? = null

        fun getDatabase(context: Context): ExerciseWorkoutDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ExerciseWorkoutDatabase::class.java,
                    "exercise_workout_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
