package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.time.LocalDate

class WorkoutHistoryViewModelTest {

    private val workoutHistory = WorkoutHistory(workoutId = 1, date = LocalDate.now())
    private val weightsExerciseHistory = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())
    private val savedWeightsExerciseHistory = WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now(), 1)

    private val mockWorkoutHistoryRepository: WorkoutHistoryRepository = Mockito.mock()
    private val mockWeightsExerciseHistoryRepository: WeightsExerciseHistoryRepository = Mockito.mock()

    private val viewModel = WorkoutHistoryViewModel(mockWorkoutHistoryRepository, mockWeightsExerciseHistoryRepository)

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveWorkoutToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.saveWorkoutHistory(workoutHistory, listOf(weightsExerciseHistory))

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).insertHistory(savedWeightsExerciseHistory)
    }

    @Test
    fun updateWorkoutInRepository() = runTest {
        viewModel.saveWorkoutHistory(workoutHistory, listOf(savedWeightsExerciseHistory), false)

        verify(mockWorkoutHistoryRepository).update(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).update(savedWeightsExerciseHistory)
    }

    @Test
    fun deleteWorkoutFromRepository() = runTest {
        viewModel.deleteWorkoutHistory(workoutHistory, listOf(savedWeightsExerciseHistory))

        verify(mockWorkoutHistoryRepository).delete(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).delete(savedWeightsExerciseHistory)
    }
}
