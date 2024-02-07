package com.example.gymtracker.ui.history

import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.history.RecordExerciseHistoryViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import java.time.LocalDate

class RecordWeightsExerciseHistoryViewModelTest {

    private val repository: WeightsExerciseHistoryRepository = Mockito.mock()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private val viewModel = RecordExerciseHistoryViewModel(repository)

    @Test
    fun saveHistoryToRepository() = runTest {
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

        viewModel.saveHistory(history)

        verify(repository).insertHistory(history)
    }

    @Test
    fun updateHistoryInRepository() = runTest {
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

        viewModel.updateHistory(history)

        verify(repository).update(history)
    }

    @Test
    fun deleteHistoryInRepository() = runTest {
        val history = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

        viewModel.deleteHistory(history)

        verify(repository).delete(history)
    }
}