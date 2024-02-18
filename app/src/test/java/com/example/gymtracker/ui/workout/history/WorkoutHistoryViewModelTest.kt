package com.example.gymtracker.ui.workout.history

import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.example.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.example.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.rules.TestCoroutineRule
import com.example.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.example.gymtracker.ui.exercise.history.state.toCardioExerciseHistory
import com.example.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.time.LocalDate

class WorkoutHistoryViewModelTest {

    private val workoutHistory = WorkoutHistory(workoutId = 1, date = LocalDate.now())
    private val weightsExerciseHistoryUI = WeightsExerciseHistoryUiState(
        id = 1,
        exerciseId = 1,
        date = LocalDate.now(),
        weight = 1.0,
        sets = 1,
        reps = 1,
        rest = 1
    )
    private val cardioExerciseHistoryUI = CardioExerciseHistoryUiState(
        id = 1,
        exerciseId = 1,
        date = LocalDate.now(),
        distance = 100.0
    )
    private val weightsExerciseHistory =
        WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now(), 1)
    private val cardioExerciseHistory =
        CardioExerciseHistory(1, 1, LocalDate.now(), distance = 100.0)
    private val savedWeightsExerciseHistory =
        WeightsExerciseHistory(1, 1, 1.0, 1, 1, LocalDate.now(), 1, 1)
    private val savedCardioExerciseHistory =
        CardioExerciseHistory(1, 1, LocalDate.now(), distance = 100.0, workoutHistoryId = 1)

    private val workoutHistoryWithExercises = WorkoutHistoryWithExercisesUiState(
        workoutHistoryId = workoutHistory.workoutHistoryId,
        workoutId = workoutHistory.workoutId,
        date = workoutHistory.date,
        exercises = listOf(weightsExerciseHistoryUI, cardioExerciseHistoryUI)
    )

    private val mockWorkoutHistoryRepository: WorkoutHistoryRepository = Mockito.mock()
    private val mockWeightsExerciseHistoryRepository: WeightsExerciseHistoryRepository =
        Mockito.mock()
    private val mockCardioExerciseHistoryRepository: CardioExerciseHistoryRepository =
        Mockito.mock()

    private val viewModel = WorkoutHistoryViewModel(
        mockWorkoutHistoryRepository,
        mockWeightsExerciseHistoryRepository,
        mockCardioExerciseHistoryRepository
    )

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveWorkoutToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.saveWorkoutHistory(workoutHistoryWithExercises, true)

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).insertHistory(savedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).insert(savedCardioExerciseHistory)
    }

    @Test
    fun updateWorkoutInRepository() = runTest {
        viewModel.saveWorkoutHistory(workoutHistoryWithExercises, false)

        verify(mockWorkoutHistoryRepository).update(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).update(weightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).update(cardioExerciseHistory)
    }

    @Test
    fun deleteWorkoutFromRepository() = runTest {
        viewModel.deleteWorkoutHistory(workoutHistoryWithExercises)

        verify(mockWorkoutHistoryRepository).delete(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).delete(weightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).delete(cardioExerciseHistory)
    }

    @Test
    fun liveSaveWorkoutHistoryToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.liveSaveWorkoutHistory(workoutHistory)

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
    }

    @Test
    fun liveSaveWeightsWorkoutExerciseHistoryToRepository() = runTest {
        viewModel.liveSaveWorkoutExerciseHistory(weightsExerciseHistoryUI)

        verify(mockWeightsExerciseHistoryRepository).insertHistory(weightsExerciseHistoryUI.toWeightsExerciseHistory())
    }

    @Test
    fun liveSaveCardioWorkoutExerciseHistoryToRepository() = runTest {
        viewModel.liveSaveWorkoutExerciseHistory(cardioExerciseHistoryUI)

        verify(mockCardioExerciseHistoryRepository).insert(cardioExerciseHistoryUI.toCardioExerciseHistory())
    }
}
