package com.example.gymtracker.ui.workout.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gymtracker.fake.FakeWorkoutWithExercisesRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class WorkoutDetailsViewModelTest {

    private val fakeRepository = FakeWorkoutWithExercisesRepository()
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

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getWorkoutWithExercisesFromRepository() = runTest {
        val viewModel = WorkoutDetailsViewModel(
            fakeRepository,
            savedState
        )

        viewModel.uiState.test {
            assertThat(awaitItem(), equalTo(workoutWithExercisesUiState))
        }
    }
}
