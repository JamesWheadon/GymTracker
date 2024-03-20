package com.askein.gymtracker.data.exerciseWithHistory

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseWithHistoryDao {
    @Transaction
    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId")
    fun getExerciseWithHistory(exerciseId: Int): Flow<ExerciseWithHistory>
}
