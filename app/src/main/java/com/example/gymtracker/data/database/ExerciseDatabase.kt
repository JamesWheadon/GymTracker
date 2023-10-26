package com.example.gymtracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseDao
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutDao
import com.example.gymtracker.data.workoutWithExercises.WorkoutWithExercisesDao
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRef
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefDao

@Database(entities = [Exercise::class, Workout::class, WorkoutExerciseCrossRef::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseCrossRefDao(): WorkoutExerciseCrossRefDao
    abstract fun workoutWithExercisesDao(): WorkoutWithExercisesDao

    companion object {
        @Volatile
        private var Instance: ExerciseDatabase? = null

        fun getDatabase(context: Context): ExerciseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ExerciseDatabase::class.java,
                    "exercise_workout_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
