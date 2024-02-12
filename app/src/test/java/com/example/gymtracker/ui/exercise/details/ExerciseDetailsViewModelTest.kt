package com.example.gymtracker.ui.exercise.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.example.gymtracker.fake.FakeCardioExerciseHistoryRepository
import com.example.gymtracker.fake.FakeExerciseRepository
import com.example.gymtracker.fake.FakeExerciseWithHistoryRepository
import com.example.gymtracker.fake.FakeWeightsExerciseHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class ExerciseDetailsViewModelTest {

    private val weightsExercise = Exercise(1, "Curls", "Biceps", "Dumbbells")
    private val cardioExercise = Exercise(2, "Running", "", "")

    private val mockExerciseRepository: ExerciseRepository = mock()
    private val mockWeightsExerciseHistoryRepository: WeightsExerciseHistoryRepository = mock()
    private val mockCardioExerciseHistoryRepository: CardioExerciseHistoryRepository = mock()
    private val mockWorkoutExerciseRepository: WorkoutExerciseCrossRefRepository = mock()
    private val fakeExerciseRepository = FakeExerciseRepository()
    private val fakeWeightsHistoryRepository = FakeWeightsExerciseHistoryRepository()
    private val fakeCardioHistoryRepository = FakeCardioExerciseHistoryRepository()
    private val fakeExerciseWithHistoryRepository = FakeExerciseWithHistoryRepository()
    private val savedState = SavedStateHandle(mapOf("exerciseId" to 1))
    private lateinit var viewModel: ExerciseDetailsViewModel

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getExerciseFromRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = fakeExerciseRepository,
            weightsExerciseHistoryRepository = fakeWeightsHistoryRepository,
            cardioExerciseHistoryRepository = fakeCardioHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            exerciseWithHistoryRepository = fakeExerciseWithHistoryRepository,
            savedStateHandle = savedState
        )
        viewModel.uiState.test {
            assertThat(
                awaitItem(), equalTo(fakeExerciseWithHistoryRepository.exerciseWithHistory.toExerciseDetailsUiState())
            )
        }
    }

    @Test
    fun updateExerciseInRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = mockExerciseRepository,
            weightsExerciseHistoryRepository = fakeWeightsHistoryRepository,
            cardioExerciseHistoryRepository = fakeCardioHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            exerciseWithHistoryRepository = fakeExerciseWithHistoryRepository,
            savedStateHandle = savedState
        )

        viewModel.updateExercise(cardioExercise)

        verify(mockExerciseRepository).updateExercise(cardioExercise)
    }

    @Test
    fun deleteWeightsExerciseInRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = mockExerciseRepository,
            weightsExerciseHistoryRepository = mockWeightsExerciseHistoryRepository,
            cardioExerciseHistoryRepository = mockCardioExerciseHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            exerciseWithHistoryRepository = fakeExerciseWithHistoryRepository,
            savedStateHandle = savedState
        )

        viewModel.deleteExercise(weightsExercise)

        verify(mockWeightsExerciseHistoryRepository).deleteAllForExercise(weightsExercise)
        verify(mockWorkoutExerciseRepository).deleteAllCrossRefForExercise(weightsExercise)
        verify(mockExerciseRepository).deleteExercise(weightsExercise)
        verifyNoInteractions(mockCardioExerciseHistoryRepository)
    }

    @Test
    fun deleteCardioExerciseInRepository() = runTest {
        viewModel = ExerciseDetailsViewModel(
            exerciseRepository = mockExerciseRepository,
            weightsExerciseHistoryRepository = mockWeightsExerciseHistoryRepository,
            cardioExerciseHistoryRepository = mockCardioExerciseHistoryRepository,
            workoutExerciseCrossRefRepository = mockWorkoutExerciseRepository,
            exerciseWithHistoryRepository = fakeExerciseWithHistoryRepository,
            savedStateHandle = savedState
        )

        viewModel.deleteExercise(cardioExercise)

        verify(mockCardioExerciseHistoryRepository).deleteAllForExercise(cardioExercise)
        verify(mockWorkoutExerciseRepository).deleteAllCrossRefForExercise(cardioExercise)
        verify(mockExerciseRepository).deleteExercise(cardioExercise)
        verifyNoInteractions(mockWeightsExerciseHistoryRepository)
    }
}
