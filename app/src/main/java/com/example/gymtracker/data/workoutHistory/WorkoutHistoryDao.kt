package com.example.gymtracker.data.workoutHistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workoutHistory: WorkoutHistory)

    @Transaction
    @Query("SELECT * FROM workout_history WHERE workoutHistoryId = :workoutHistoryId")
    fun getExercisesForWorkoutHistory(workoutHistoryId: Int): Flow<WorkoutHistoryWithExerciseHistory>
}
