package com.example.gymtracker.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exerciseHistory: ExerciseHistory)

    @Query("SELECT * from history WHERE id = :id")
    fun getHistory(id: Int): Flow<ExerciseHistory>
}
