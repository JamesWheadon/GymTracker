package com.example.gymtracker.data.history

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class OfflineHistoryRepository(private val historyDao: HistoryDao) : HistoryRepository {
    override fun getDatesStream(): Flow<List<Long>> = combine(
        historyDao.getWorkoutHistoryDates(),
        historyDao.getWeightsHistoryDates(),
        historyDao.getCardioHistoryDates()
    ) { workoutDates, weightsDates, cardioDates ->
        workoutDates + weightsDates + cardioDates
    }

    override fun getWorkoutsForDate(date: Long): Flow<List<Workout>> =
        historyDao.getWorkoutsForDate(date)

    override fun getExercisesForDate(date: Long): Flow<List<Exercise>> =
        historyDao.getExercisesForDate(date)
}
