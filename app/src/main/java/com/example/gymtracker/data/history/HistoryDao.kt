package com.example.gymtracker.data.history

import androidx.room.Dao
import androidx.room.Query
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT DISTINCT date FROM workout_history")
    fun getWorkoutHistoryDates(): Flow<List<Long>>

    @Query("SELECT DISTINCT date FROM cardio_exercise_history WHERE workoutHistoryId is null")
    fun getCardioHistoryDates(): Flow<List<Long>>

    @Query("SELECT DISTINCT date FROM weights_exercise_history WHERE workoutHistoryId is null")
    fun getWeightsHistoryDates(): Flow<List<Long>>

    @Query("SELECT * FROM workouts WHERE workoutId IN (SELECT workoutId FROM workout_history WHERE date = :date)")
    fun getWorkoutsForDate(date: Long): Flow<List<Workout>>

    @Query("SELECT * FROM exercises WHERE exerciseId IN (SELECT exerciseId FROM cardio_exercise_history WHERE date = :date AND workoutHistoryId is null) OR exerciseId IN (SELECT exerciseId FROM weights_exercise_history WHERE date = :date AND workoutHistoryId is null)")
    fun getExercisesForDate(date: Long): Flow<List<Exercise>>
}
