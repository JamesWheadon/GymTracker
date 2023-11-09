package com.example.gymtracker.data.workoutWithExercises

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutWithExercisesDao {
    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getWorkoutWithExercises(workoutId: Int): Flow<WorkoutWithExercises>
}