package com.example.gymtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymtracker.converters.LocalDateConverter
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseDao
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryDao
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryDao
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutDao
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefDao
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryDao
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesDao

@Database(
    entities = [
        Exercise::class,
        WeightsExerciseHistory::class,
        CardioExerciseHistory::class,
        Workout::class,
        WorkoutExerciseCrossRef::class,
        WorkoutHistory::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class ExerciseWorkoutDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun weightsExerciseHistoryDao(): WeightsExerciseHistoryDao
    abstract fun cardioExerciseHistoryDao(): CardioExerciseHistoryDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseCrossRefDao(): WorkoutExerciseCrossRefDao
    abstract fun workoutWithExercisesDao(): WorkoutWithExercisesDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao

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
