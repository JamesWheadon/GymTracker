package com.askein.gymtracker.ui.workout.history

import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.cardio.CardioExerciseHistoryRepository
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistory
import com.askein.gymtracker.data.exerciseHistory.weights.WeightsExerciseHistoryRepository
import com.askein.gymtracker.data.workoutHistory.WorkoutHistory
import com.askein.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.exercise.history.state.toCardioExerciseHistory
import com.askein.gymtracker.ui.exercise.history.state.toWeightsExerciseHistory
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import java.time.LocalDate

class WorkoutHistoryViewModelTest {

    private val workoutHistory = WorkoutHistory(workoutHistoryId = 1, workoutId = 1, date = LocalDate.now())
    private val workoutHistoryUiState = WorkoutHistoryUiState(workoutHistoryId = 1, workoutId = 1)
    private val weightsExerciseHistoryUI = WeightsExerciseHistoryUiState(id = 1, exerciseId = 1, workoutHistoryId = 1)
    private val cardioExerciseHistoryUI = CardioExerciseHistoryUiState(id = 1, exerciseId = 2, workoutHistoryId = 1)
    private val newWeightsExerciseHistoryUI = WeightsExerciseHistoryUiState(exerciseId = 3)
    private val newCardioExerciseHistoryUI = CardioExerciseHistoryUiState(exerciseId = 4)
    private val oldWeightsExerciseHistoryUI = WeightsExerciseHistoryUiState(id = 3, exerciseId = 5, workoutHistoryId = 1)
    private val oldCardioExerciseHistoryUI = CardioExerciseHistoryUiState(id = 3, exerciseId = 6, workoutHistoryId = 1)
    private val savedWeightsExerciseHistory =
        WeightsExerciseHistory(1, 1, 0.0, 0, 0, LocalDate.now(), null, 1)
    private val savedCardioExerciseHistory =
        CardioExerciseHistory(1, 2, LocalDate.now(), workoutHistoryId = 1)
    private val newSavedWeightsExerciseHistory =
        WeightsExerciseHistory(0, 3, 0.0, 0, 0, LocalDate.now(), null, 1)
    private val newSavedCardioExerciseHistory =
        CardioExerciseHistory(0, 4, LocalDate.now(), workoutHistoryId = 1)
    private val oldSavedWeightsExerciseHistory =
        WeightsExerciseHistory(3, 5, 0.0, 0, 0, LocalDate.now(), null, 1)
    private val oldSavedCardioExerciseHistory =
        CardioExerciseHistory(3, 6, LocalDate.now(), workoutHistoryId = 1)

    private val workoutHistoryWithExercises = WorkoutHistoryWithExercisesUiState(
        workoutHistoryId = workoutHistory.workoutHistoryId,
        workoutId = workoutHistory.workoutId,
        date = workoutHistory.date,
        exercises = listOf(weightsExerciseHistoryUI, cardioExerciseHistoryUI, newWeightsExerciseHistoryUI, newCardioExerciseHistoryUI)
    )

    private val existingWorkoutHistory = WorkoutHistoryWithExercisesUiState(
        workoutHistoryId = workoutHistory.workoutHistoryId,
        workoutId = workoutHistory.workoutId,
        date = workoutHistory.date,
        exercises = listOf(weightsExerciseHistoryUI, cardioExerciseHistoryUI, oldWeightsExerciseHistoryUI, oldCardioExerciseHistoryUI)
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

        viewModel.saveWorkoutHistory(workoutHistoryWithExercises, WorkoutHistoryWithExercisesUiState(), true)

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).insert(savedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).insert(savedCardioExerciseHistory)
    }

    @Test
    fun updateWorkoutInRepository() = runTest {
        viewModel.saveWorkoutHistory(workoutHistoryWithExercises, existingWorkoutHistory, false)

        verify(mockWeightsExerciseHistoryRepository).update(savedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).update(savedCardioExerciseHistory)
        verify(mockWeightsExerciseHistoryRepository).insert(newSavedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).insert(newSavedCardioExerciseHistory)
        verify(mockWeightsExerciseHistoryRepository).delete(oldSavedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).delete(oldSavedCardioExerciseHistory)
        verifyNoMoreInteractions(mockWeightsExerciseHistoryRepository)
        verifyNoMoreInteractions(mockCardioExerciseHistoryRepository)
    }

    @Test
    fun deleteWorkoutFromRepository() = runTest {
        viewModel.deleteWorkoutHistory(workoutHistoryWithExercises)

        verify(mockWorkoutHistoryRepository).delete(workoutHistory)
        verify(mockWeightsExerciseHistoryRepository).delete(savedWeightsExerciseHistory)
        verify(mockCardioExerciseHistoryRepository).delete(savedCardioExerciseHistory)
    }

    @Test
    fun liveSaveWorkoutHistoryToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.liveSaveWorkoutHistory(workoutHistoryUiState)

        verify(mockWorkoutHistoryRepository).insert(workoutHistory)
    }

    @Test
    fun liveSaveWeightsWorkoutExerciseHistoryToRepository() = runTest {
        viewModel.liveSaveWorkoutExerciseHistory(weightsExerciseHistoryUI)

        verify(mockWeightsExerciseHistoryRepository).insert(weightsExerciseHistoryUI.toWeightsExerciseHistory())
    }

    @Test
    fun liveSaveCardioWorkoutExerciseHistoryToRepository() = runTest {
        viewModel.liveSaveWorkoutExerciseHistory(cardioExerciseHistoryUI)

        verify(mockCardioExerciseHistoryRepository).insert(cardioExerciseHistoryUI.toCardioExerciseHistory())
    }

    @Test
    fun liveDeleteWorkoutHistoryToRepository() = runTest {
        `when`(mockWorkoutHistoryRepository.insert(workoutHistory)).thenReturn(1L)

        viewModel.liveSaveWorkoutHistory(workoutHistoryUiState)
        viewModel.liveDeleteWorkoutHistory()

        verify(mockWorkoutHistoryRepository).delete(1)
        verify(mockWeightsExerciseHistoryRepository).deleteAllForWorkoutHistory(1)
        verify(mockCardioExerciseHistoryRepository).deleteAllForWorkoutHistory(1)
    }
}