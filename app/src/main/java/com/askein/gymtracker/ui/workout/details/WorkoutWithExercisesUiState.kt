package com.askein.gymtracker.ui.workout.details

import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workoutWithExercises.WorkoutWithExercises
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.toExerciseUiState
import com.askein.gymtracker.ui.workout.WorkoutUiState
import com.askein.gymtracker.ui.workout.history.WorkoutHistoryWithExercisesUiState
import com.askein.gymtracker.ui.workout.history.toWorkoutHistoryWithExercisesUiState

data class WorkoutWithExercisesUiState(
    val workoutId: Int = 0,
    val name: String = "",
    val exercises: List<ExerciseUiState> = listOf(),
    val workoutHistory: List<WorkoutHistoryWithExercisesUiState> = listOf()
)

fun WorkoutWithExercises.toWorkoutWithExercisesUiState(): WorkoutWithExercisesUiState =
    WorkoutWithExercisesUiState(
        workoutId = workout.workoutId,
        name = workout.name,
        exercises = exercises
            .map { exercise -> exercise.toExerciseUiState() }
            .sortedBy { exercise ->
                exerciseOrder.first { crossRef -> crossRef.exerciseId == exercise.id }.order
            },
        workoutHistory = workoutHistory.map { workoutHistory ->
            workoutHistory.toWorkoutHistoryWithExercisesUiState()
        }
    )

fun WorkoutWithExercisesUiState.toWorkoutUiState(): WorkoutUiState = WorkoutUiState(
    workoutId = workoutId,
    name = name
)

fun WorkoutWithExercisesUiState.toWorkout(): Workout = Workout(
    workoutId = workoutId,
    name = name
)
