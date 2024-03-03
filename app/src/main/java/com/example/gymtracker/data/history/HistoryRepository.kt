package com.example.gymtracker.data.history

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getDatesStream(): Flow<List<Long>>

    fun getWorkoutsForDate(date: Long): Flow<List<Workout>>

    fun getExercisesForDate(date: Long): Flow<List<Exercise>>
}