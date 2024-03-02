package com.example.gymtracker.data.exerciseHistory.weights

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightsExerciseHistoryDao {
    @Query("SELECT * FROM weights_exercise_history WHERE id = :id")
    fun getHistory(id: Int): Flow<WeightsExerciseHistory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weightsExerciseHistory: WeightsExerciseHistory)

    @Update
    suspend fun update(weightsExerciseHistory: WeightsExerciseHistory)

    @Delete
    suspend fun delete(weightsExerciseHistory: WeightsExerciseHistory)

    @Query("DELETE FROM weights_exercise_history WHERE exerciseId = :exerciseId")
    suspend fun deleteAllForExercise(exerciseId: Int)
}
