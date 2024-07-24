package com.askein.gymtracker.ui.workout.details

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.toExercise
import com.askein.gymtracker.ui.exercise.toExerciseUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

private const val WORKOUT_NAME = "test workout"

class WorkoutExerciseCrossRefViewModelTest {

    private val mockRepository: WorkoutExerciseCrossRefRepository = mock()
    private val exercise = Exercise(
        exerciseId = 1,
        exerciseType = ExerciseType.WEIGHTS,
        name = "test name",
        muscleGroup = "test muscle",
        equipment = "test kit"
    )
    private val exerciseUiState = exercise.toExerciseUiState()
    private val workout = Workout(workoutId = 1, name = WORKOUT_NAME)
    private val workoutWithExercisesUiState = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = WORKOUT_NAME,
        exercises = listOf(exerciseUiState)
    )
    private lateinit var viewModel: WorkoutExerciseCrossRefViewModel

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveExercisesForWorkoutSavesExercisesToWorkout() = runTest {
        viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

        viewModel.saveExercisesForWorkout(
            exercises = listOf(exerciseUiState),
            workout = workoutWithExercisesUiState
        )

        verify(mockRepository).saveExercisesToWorkout(listOf(exercise), workout)
        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun saveExercisesForWorkoutSavesExercisesToWorkoutDeletesRemovedExercises() {
        val newExercise = ExerciseUiState(name = "Bench")
        runTest {
            viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

            viewModel.saveExercisesForWorkout(
                exercises = listOf(newExercise),
                workout = workoutWithExercisesUiState
            )

            verify(mockRepository).saveExercisesToWorkout(listOf(newExercise.toExercise()), workout)
            verify(mockRepository).deleteExercisesFromWorkout(listOf(exercise), workout)
        }
    }
}