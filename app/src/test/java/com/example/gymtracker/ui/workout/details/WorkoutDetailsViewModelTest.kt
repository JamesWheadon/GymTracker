package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.fake.FakeWorkoutWithExercisesRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class WorkoutDetailsViewModelTest {

    private val fakeWorkoutWithExercisesRepository = FakeWorkoutWithExercisesRepository()
    private val mockWorkoutRepository: WorkoutRepository = mock()
    private val mockWorkoutExerciseRepository: WorkoutExerciseCrossRefRepository = mock()
    private val savedState = SavedStateHandle(mapOf("workoutId" to 1))

    private val workoutWithExercisesUiState = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = "Test",
        exercises = listOf(
            ExerciseUiState(
                id = 1,
                name = "testName",
                muscleGroup = "muscleGroup",
                equipment = "equipment"
            )
        )
    )
    private val workout = Workout(
        workoutId = 1,
        name = "Test"
    )

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getWorkoutWithExercisesFromRepository() = runTest {
        val viewModel = WorkoutDetailsViewModel(
            workoutRepository = mockWorkoutRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )

        viewModel.uiState.test {
            assertThat(awaitItem(), equalTo(workoutWithExercisesUiState))
        }
    }

    @Test
    fun updateWorkoutInRepository() = runTest {
        val viewModel = WorkoutDetailsViewModel(
            workoutRepository = mockWorkoutRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )

        viewModel.updateWorkout(workout)

        verify(mockWorkoutRepository).updateWorkout(workout)
    }

    @Test
    fun deleteWorkoutInRepository() = runTest {
        val viewModel = WorkoutDetailsViewModel(
            workoutRepository = mockWorkoutRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )

        viewModel.deleteWorkout(workout)

        verify(mockWorkoutRepository).deleteWorkout(workout)
        verify(mockWorkoutExerciseRepository).deleteAllCrossRefForWorkout(workout)
    }
}
