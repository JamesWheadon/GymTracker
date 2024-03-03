package com.example.gymtracker.fake

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.data.workout.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeHistoryRepository: HistoryRepository {

    private val datesFlow = MutableSharedFlow<List<Long>>()
    private val dateWorkoutFlow = MutableSharedFlow<List<Workout>>()
    private val dateExerciseFlow = MutableSharedFlow<List<Exercise>>()

    suspend fun emitDates(value: List<Long>) {
        datesFlow.emit(value)
    }

    suspend fun emitAllWorkouts(value: List<Workout>) {
        dateWorkoutFlow.emit(value)
    }

    suspend fun emitAllExercises(value: List<Exercise>) {
        dateExerciseFlow.emit(value)
    }

    override fun getDatesStream(): Flow<List<Long>> = datesFlow

    override fun getWorkoutsForDate(date: Long): Flow<List<Workout>> = dateWorkoutFlow

    override fun getExercisesForDate(date: Long): Flow<List<Exercise>> = dateExerciseFlow
}
