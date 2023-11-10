package com.example.gymtracker.data.workoutExerciseCrossRef

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutExerciseCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutExercise: WorkoutExerciseCrossRef)

    @Delete
    suspend fun delete(workoutExercise: WorkoutExerciseCrossRef)

    @Query("DELETE FROM workouts_exercises WHERE workoutId = :workoutId")
    suspend fun deleteAllCrossRefForWorkout(workoutId: Int)

    @Query("DELETE FROM workouts_exercises WHERE exerciseId = :exerciseId")
    suspend fun deleteAllCrossRefForExercise(exerciseId: Int)
}
