package com.askein.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workout.WorkoutRepository
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.fake.FakeWorkoutWithExercisesRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.workout.toWorkoutUiState
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
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
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
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
            savedStateHandle = savedState
        )

        viewModel.updateWorkout(workout.toWorkoutUiState())

        verify(mockWorkoutRepository).updateWorkout(workout)
    }

    @Test
    fun deleteWorkoutInRepository() = runTest {
        val viewModel = WorkoutDetailsViewModel(
            workoutRepository = mockWorkoutRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            workoutWithExercisesRepository = fakeWorkoutWithExercisesRepository,
            savedStateHandle = savedState
        )

        viewModel.deleteWorkout(workout.toWorkoutUiState())

        verify(mockWorkoutRepository).deleteWorkout(workout)
        verify(mockWorkoutExerciseRepository).deleteAllCrossRefForWorkout(workout)
    }
}
