package com.example.gymtracker.data.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseHistoryDao {
    @Query("SELECT * FROM exercise_history WHERE id = :id")
    fun getHistory(id: Int): Flow<ExerciseHistory>

    @Query("SELECT * FROM exercise_history WHERE exerciseId = :exerciseId")
    fun getFullExerciseHistory(exerciseId: Int): Flow<List<ExerciseHistory>>

    @Query("SELECT * FROM exercise_history WHERE exerciseId = :exerciseId ORDER BY date DESC LIMIT 1")
    fun getLatestExerciseHistory(exerciseId: Int): Flow<ExerciseHistory>

    @Query("SELECT * FROM exercise_history WHERE exerciseId = :exerciseId AND date > strftime('%s','now') / 86400 - :days")
    fun getRecentExerciseHistory(exerciseId: Int, days: Int): Flow<List<ExerciseHistory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exerciseHistory: ExerciseHistory)

    @Update
    suspend fun update(exerciseHistory: ExerciseHistory)

    @Delete
    suspend fun delete(exerciseHistory: ExerciseHistory)

    @Query("DELETE FROM exercise_history WHERE exerciseId = :exerciseId")
    suspend fun deleteAllForExercise(exerciseId: Int)
}
