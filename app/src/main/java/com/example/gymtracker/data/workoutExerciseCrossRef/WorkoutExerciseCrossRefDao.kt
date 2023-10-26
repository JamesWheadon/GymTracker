package com.example.gymtracker.data.workoutExerciseCrossRef

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface WorkoutExerciseCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutExercise: WorkoutExerciseCrossRef)
}
