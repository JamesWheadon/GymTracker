package com.askein.gymtracker.ui.history

import app.cash.turbine.test
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.history.HistoryRepository
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.fake.FakeHistoryRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.workout.WorkoutUiState
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
    fun getWorkoutForDateFromRepository() = runTest {
        val viewModel = OverallHistoryViewModel(fakeRepository)

        viewModel.workoutsOnDateUiState.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeRepository.emitAllWorkouts(listOf(Workout(name = "Arms")))

            val item = awaitItem()
            assertThat(item[0].javaClass, equalTo(WorkoutUiState::class.java))
            assertThat(item[0].name, equalTo("Arms"))
        }
    }

    @Test
    fun getExerciseForDateFromRepository() = runTest {
        val viewModel = OverallHistoryViewModel(fakeRepository)

        viewModel.exercisesOnDateUiState.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeRepository.emitAllExercises(
                listOf(
                    Exercise(
                        name = "Curls",
                        muscleGroup = "",
                        equipment = ""
                    )
                )
            )

            val item = awaitItem()
            assertThat(item[0].javaClass, equalTo(ExerciseUiState::class.java))
            assertThat(item[0].name, equalTo("Curls"))
        }
    }

    @Test
    fun updateSelectedDateGetsWorkoutAndExercisesForDate() = runBlocking {
        `when`(mockRepository.getExercisesForDate(LocalDate.now().toEpochDay())).thenReturn(
            flowOf(listOf(Exercise(name = "", muscleGroup = "", equipment = "")))
        )
        `when`(mockRepository.getWorkoutsForDate(LocalDate.now().toEpochDay())).thenReturn(
            flowOf(listOf(Workout(name = "")))
        )

        val viewModel = OverallHistoryViewModel(mockRepository)

        viewModel.selectDate(LocalDate.now())

        val workouts = viewModel.workoutsOnDateUiState.first()
        val exercises = viewModel.exercisesOnDateUiState.first()

        verify(mockRepository).getExercisesForDate(LocalDate.now().toEpochDay())
        verify(mockRepository).getWorkoutsForDate(LocalDate.now().toEpochDay())
        assertThat(workouts.size, equalTo(1))
        assertThat(exercises.size, equalTo(1))
    }
}
