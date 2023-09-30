package com.example.gymtracker.ui.history

import com.example.gymtracker.data.history.ExerciseHistory
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import java.time.LocalDate

class RecordHistoryViewModelTest {

    private val repository: HistoryRepository = Mockito.mock()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveHistoryToRepository() = runTest {
        val viewModel = RecordHistoryViewModel(repository)
        val history = ExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())

        viewModel.saveHistory(history)

        verify(repository).insertHistory(history)
    }
}