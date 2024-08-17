package com.askein.gymtracker.ui.exercise.history

import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import java.time.LocalDate

class RecordWeightsExerciseHistoryViewModelTest {

    private val weightsRepository: WeightsExerciseHistoryRepository = Mockito.mock()
    private val cardioRepository: CardioExerciseHistoryRepository = Mockito.mock()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val viewModel = RecordExerciseHistoryViewModel(weightsRepository, cardioRepository)

    @Test
    fun saveWeightsHistoryToRepository() = runTest {
        val history = WeightsExerciseHistory(
            id = 1,
            exerciseId = 1,
            weight = listOf(1.0),
            sets = 1,
            reps = listOf(1),
            date = LocalDate.now()
        )

        viewModel.saveHistory(history.toWeightsExerciseHistoryUiState())

        verify(weightsRepository).insert(history)
    }

    @Test
    fun saveCardioHistoryToRepository() = runTest {
        val history = CardioExerciseHistory(1, 1, LocalDate.now())

        viewModel.saveHistory(history.toCardioExerciseHistoryUiState())

        verify(cardioRepository).insert(history)
    }

    @Test
    fun updateWeightsHistoryInRepository() = runTest {
        val history = WeightsExerciseHistory(
            id = 1,
            exerciseId = 1,
            weight = listOf(1.0),
            sets = 1,
            reps = listOf(1),
            date = LocalDate.now()
        )

        viewModel.updateHistory(history.toWeightsExerciseHistoryUiState())

        verify(weightsRepository).update(history)
    }

    @Test
    fun updateCardioHistoryInRepository() = runTest {
        val history = CardioExerciseHistory(1, 1, LocalDate.now())

        viewModel.updateHistory(history.toCardioExerciseHistoryUiState())

        verify(cardioRepository).update(history)
    }

    @Test
    fun deleteWeightsHistoryInRepository() = runTest {
        val history = WeightsExerciseHistory(
            id = 1,
            exerciseId = 1,
            weight = listOf(1.0),
            sets = 1,
            reps = listOf(1),
            date = LocalDate.now()
        )

        viewModel.deleteHistory(history.toWeightsExerciseHistoryUiState())

        verify(weightsRepository).delete(history)
    }

    @Test
    fun deleteCardioHistoryInRepository() = runTest {
        val history = CardioExerciseHistory(1, 1, LocalDate.now())

        viewModel.deleteHistory(history.toCardioExerciseHistoryUiState())

        verify(cardioRepository).delete(history)
    }
}