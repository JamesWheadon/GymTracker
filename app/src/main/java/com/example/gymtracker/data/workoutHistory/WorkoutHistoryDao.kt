package com.example.gymtracker.data.workoutHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutHistory: WorkoutHistory): Long
}
