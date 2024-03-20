package com.askein.gymtracker.fake

import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workout.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeWorkoutRepository : WorkoutRepository {

    private val allWorkoutsFlow = MutableSharedFlow<List<Workout>>()

    suspend fun emitAllWorkouts(value: List<Workout>) {
        allWorkoutsFlow.emit(value)
    }

    override fun getAllWorkoutsStream(): Flow<List<Workout>> = allWorkoutsFlow

    override suspend fun insertWorkout(workout: Workout) {
    }

    override suspend fun updateWorkout(workout: Workout) {
    }

    override suspend fun deleteWorkout(workout: Workout) {
    }
}
