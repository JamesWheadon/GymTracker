package com.askein.gymtracker.data.history

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.workout.Workout
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getDatesStream(): Flow<List<Long>>

    fun getWorkoutsForDate(date: Long): Flow<List<Workout>>

    fun getExercisesForDate(date: Long): Flow<List<Exercise>>
}