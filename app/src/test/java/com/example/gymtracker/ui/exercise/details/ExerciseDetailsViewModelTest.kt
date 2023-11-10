package com.example.gymtracker.ui.exercise.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.ExerciseHistoryRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.fake.FakeExerciseRepository
import com.example.gymtracker.fake.FakeExerciseHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.exercise1
import com.example.gymtracker.ui.exercise.toExerciseDetailsUiState
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import java.time.LocalDate

class ExerciseDetailsViewModelTest {

    private val mockExerciseRepository: ExerciseRepository = mock()
    private val mockExerciseHistoryRepository: ExerciseHistoryRepository = mock()
    private val mockWorkoutExerciseRepository: WorkoutExerciseCrossRefRepository = mock()
    private val fakeExerciseRepository = FakeExerciseRepository()
    private val fakeHistoryRepository = FakeExerciseHistoryRepository()
    private val savedState = SavedStateHandle(mapOf("exerciseId" to 1))
    private lateinit var viewModel: ExerciseDetailsViewModel

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getExerciseFromRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = fakeExerciseRepository,
            exerciseHistoryRepository = fakeHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )
        viewModel.uiState.test {
            assertThat(
                awaitItem(), equalTo(fakeExerciseRepository.exercise.toExerciseDetailsUiState())
            )
        }
    }

    @Test
    fun getHistoryFromRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = fakeExerciseRepository,
            exerciseHistoryRepository = fakeHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )
        viewModel.exerciseHistory.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeHistoryRepository.emitAllHistory(listOf(getExerciseHistory(1), getExerciseHistory(2)))

            assertThat(awaitItem().size, equalTo(2))
        }
    }

    @Test
    fun updateExerciseInRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = mockExerciseRepository,
            exerciseHistoryRepository = fakeHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )

        viewModel.updateExercise(exercise1)

        verify(mockExerciseRepository).updateExercise(exercise1)
    }

    @Test
    fun deleteExerciseInRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = mockExerciseRepository,
            exerciseHistoryRepository = mockExerciseHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            savedStateHandle = savedState
        )

        viewModel.deleteExercise(exercise1)

        verify(mockExerciseHistoryRepository).deleteAllForExercise(exercise1)
        verify(mockWorkoutExerciseRepository).deleteAllCrossRefForExercise(exercise1)
        verify(mockExerciseRepository).deleteExercise(exercise1)
    }

    private fun getExerciseHistory(id: Int) = ExerciseHistory(id, 1, 10.0, 10, 10, LocalDate.now())
}
