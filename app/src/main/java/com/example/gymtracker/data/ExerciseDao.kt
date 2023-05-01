package com.example.gymtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Update
    suspend fun update(exercise: Exercise)

    @Query("SELECT * from exercises WHERE id = :id")
    fun getExercise(id: Int): Flow<Exercise>

    @Query("SELECT * from exercises")
    fun getAllExercises(): Flow<List<Exercise>>
}
