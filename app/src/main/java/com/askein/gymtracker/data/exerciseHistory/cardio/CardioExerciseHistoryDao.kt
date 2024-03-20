package com.askein.gymtracker.data.exerciseHistory.cardio

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CardioExerciseHistoryDao {
    @Query("SELECT * FROM cardio_exercise_history WHERE id = :id")
    fun get(id: Int): Flow<CardioExerciseHistory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cardioExerciseHistory: CardioExerciseHistory)

    @Update
    suspend fun update(cardioExerciseHistory: CardioExerciseHistory)

    @Delete
    suspend fun delete(cardioExerciseHistory: CardioExerciseHistory)

    @Query("DELETE FROM cardio_exercise_history WHERE exerciseId = :exerciseId")
    suspend fun deleteAllForExercise(exerciseId: Int)
}
