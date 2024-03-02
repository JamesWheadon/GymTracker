package com.example.gymtracker.ui.workout.details

import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.workout.toWorkoutUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class WorkoutExerciseCrossRefViewModelTest {

    private val mockRepository: WorkoutExerciseCrossRefRepository = mock()
    private val exercise = Exercise(1, "test name", "test muscle", "test kit")
    private val workout = Workout(1, "test workout")
    private lateinit var viewModel: WorkoutExerciseCrossRefViewModel

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun insertWorkoutExerciseCrossRefInToRepository() = runTest {
        viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

        viewModel.saveExerciseToWorkout(exercise.toExerciseUiState(), workout.toWorkoutUiState())

        verify(mockRepository).saveExerciseToWorkout(exercise, workout)
    }

    @Test
    fun deleteWorkoutExerciseCrossRefFromRepository() = runTest {
        viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

        viewModel.deleteExerciseFromWorkout(
            exercise.toExerciseUiState(),
            workout.toWorkoutUiState()
        )

        verify(mockRepository).deleteExerciseFromWorkout(exercise, workout)
    }
}