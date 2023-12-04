package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.exerciseHistory.ExerciseHistory
import com.example.gymtracker.data.exerciseHistory.ExerciseHistoryRepository
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
    private val exerciseHistory = ExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now())
    private val savedExerciseHistory = ExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now(), 1)

    private val mockWorkoutHistoryRepository: WorkoutHistoryRepository = Mockito.mock()
    private val mockExerciseHistoryRepository: ExerciseHistoryRepository = Mockito.mock()

    private val viewModel = WorkoutHistoryViewModel(mockWorkoutHistoryRepository, mockExerciseHistoryRepository)

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveWorkoutToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.saveWorkoutHistory(workoutHistory, listOf(exerciseHistory))

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
        verify(mockExerciseHistoryRepository).insertHistory(savedExerciseHistory)
    }

    @Test
    fun updateWorkoutInRepository() = runTest {
        viewModel.saveWorkoutHistory(workoutHistory, listOf(savedExerciseHistory), false)

        verify(mockWorkoutHistoryRepository).update(workoutHistory)
        verify(mockExerciseHistoryRepository).update(savedExerciseHistory)
    }

    @Test
    fun deleteWorkoutFromRepository() = runTest {
        viewModel.deleteWorkoutHistory(workoutHistory, listOf(savedExerciseHistory))

        verify(mockWorkoutHistoryRepository).delete(workoutHistory)
        verify(mockExerciseHistoryRepository).delete(savedExerciseHistory)
    }
}
