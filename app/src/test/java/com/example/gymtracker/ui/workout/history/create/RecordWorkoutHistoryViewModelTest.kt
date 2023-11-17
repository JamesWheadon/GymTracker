package com.example.gymtracker.ui.workout.history.create

import com.example.gymtracker.data.workoutHistory.WorkoutHistory
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryRepository
import com.example.gymtracker.data.workoutHistory.WorkoutHistoryWithExerciseHistory
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import java.time.LocalDate

class RecordWorkoutHistoryViewModelTest {

    private val workoutHistory = WorkoutHistoryWithExerciseHistory(
        workoutHistory = WorkoutHistory(1, 1, LocalDate.now()),
        exercises = listOf()
    )

    private val mockRepository: WorkoutHistoryRepository = Mockito.mock()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun saveWorkoutToRepository() = runTest {
        val viewModel = RecordWorkoutHistoryViewModel(mockRepository)

        viewModel.saveWorkout(workoutHistory)

        verify(mockRepository).insert(workoutHistory)
    }
}