package com.example.gymtracker.ui.workout

import app.cash.turbine.test
import com.example.gymtracker.data.workout.Workout
import com.example.gymtracker.data.workout.WorkoutRepository
import com.example.gymtracker.fake.FakeWorkoutRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

val workout1 = Workout(1, "first")
val workout2 = Workout(2, "second")

val workout1Ui = WorkoutUiState(1, "first")
val workout2Ui = WorkoutUiState(2, "second")

class WorkoutScreenViewModelTest {

    private val fakeRepository = FakeWorkoutRepository()
    private val mockRepository: WorkoutRepository = mock()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getWorkoutListFromRepository() = runTest {
        val viewModel = WorkoutScreenViewModel(fakeRepository)

        viewModel.workoutListUiState.test {
            assertThat(awaitItem().workoutList, equalTo(listOf()))

            fakeRepository.emitAllWorkouts(listOf(workout1, workout2))

            assertThat(awaitItem().workoutList, equalTo(listOf(workout1Ui, workout2Ui)))
        }
    }

    @Test
    fun saveWorkoutToRepository() = runTest {
        val viewModel = WorkoutScreenViewModel(mockRepository)

        viewModel.saveWorkout(workout1.toWorkoutUiState())

        verify(mockRepository).insertWorkout(workout1)
    }
}
