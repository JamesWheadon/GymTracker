package com.askein.gymtracker.data.workoutHistory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutHistory: WorkoutHistory): Long

    @Update
    suspend fun update(workoutHistory: WorkoutHistory)

    @Delete
    suspend fun delete(workoutHistory: WorkoutHistory)

    @Query("DELETE FROM workout_history WHERE workoutHistoryId = :workoutHistoryId")
    suspend fun delete(workoutHistoryId: Int)
}
