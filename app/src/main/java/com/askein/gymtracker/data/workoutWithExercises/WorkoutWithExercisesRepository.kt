package com.askein.gymtracker.data.workoutWithExercises

import kotlinx.coroutines.flow.Flow

interface WorkoutWithExercisesRepository {
    fun getWorkoutWithExercisesStream(workoutId: Int): Flow<WorkoutWithExercises>
}
