package com.example.gymtracker.ui.history

import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import com.example.gymtracker.ui.exercise.history.state.toCardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistoryUiState
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
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

        viewModel.saveHistory(history.toWeightsExerciseHistoryUiState())

        verify(weightsRepository).insertHistory(history)
    }

    @Test
    fun saveCardioHistoryToRepository() = runTest {
        val history = CardioExerciseHistory(1, 1, LocalDate.now())

        viewModel.saveHistory(history.toCardioExerciseHistoryUiState())

        verify(cardioRepository).insert(history)
    }

    @Test
    fun updateWeightsHistoryInRepository() = runTest {
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

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
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

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
