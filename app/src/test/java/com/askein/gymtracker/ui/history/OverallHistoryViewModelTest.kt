package com.askein.gymtracker.ui.history

import app.cash.turbine.test
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.data.history.HistoryRepository
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.fake.FakeHistoryRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.toExerciseUiState
import com.askein.gymtracker.ui.workout.toWorkoutUiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.LocalDate

class OverallHistoryViewModelTest {

    private val fakeRepository = FakeHistoryRepository()
    private val mockRepository: HistoryRepository = mock()

    private val workouts = listOf(Workout(name = "Arms"))
    private val exercises = listOf(
        Exercise(
            name = "Curls",
            exerciseType = ExerciseType.WEIGHTS,
            muscleGroup = "",
            equipment = ""
        )
    )

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getDatesListFromRepository() = runTest {
        val viewModel = OverallHistoryViewModel(fakeRepository)

        viewModel.datesUiState.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeRepository.emitDates(listOf(LocalDate.now().toEpochDay()))

            assertThat(awaitItem()[0], equalTo(LocalDate.now()))
        }
    }

    @Test
    fun updateSelectedDateGetsWorkoutAndExercisesForDate() = runBlocking {
        `when`(mockRepository.getWorkoutsForDate(LocalDate.now().toEpochDay())).thenReturn(
            flowOf(workouts)
        )
        `when`(mockRepository.getExercisesForDate(LocalDate.now().toEpochDay())).thenReturn(
            flowOf(exercises)
        )

        val viewModel = OverallHistoryViewModel(mockRepository)

        viewModel.selectDate(LocalDate.now())

        val history = viewModel.historyUiState.first()

        verify(mockRepository).getExercisesForDate(LocalDate.now().toEpochDay())
        verify(mockRepository).getWorkoutsForDate(LocalDate.now().toEpochDay())
        assertThat(history.workouts, equalTo(workouts.map { it.toWorkoutUiState() }))
        assertThat(history.exercises, equalTo(exercises.map { it.toExerciseUiState() }))
    }
}
