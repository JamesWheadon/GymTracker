package com.example.gymtracker.ui.exercise

import app.cash.turbine.test
import com.example.gymtracker.data.exercise.Exercise
import com.example.gymtracker.data.exercise.ExerciseRepository
import com.example.gymtracker.fake.FakeExerciseRepository
import com.example.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

val exercise1 = Exercise(1, "Curls", "Biceps", "Dumbbells")
val exercise2 = Exercise(2, "Dips", "Triceps", "Dumbbells")

class ExercisesScreenViewModelTest {

    private val repository: ExerciseRepository = mock()
    private val fakeRepository = FakeExerciseRepository()

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun getExerciseListFromRepository() = runTest {
        val viewModel = ExercisesScreenViewModel(fakeRepository)

        viewModel.exerciseListUiState.test {
            assertThat(awaitItem().exerciseList.size, equalTo(0))

            fakeRepository.emitAllExercises(listOf(exercise1, exercise2))

            assertThat(awaitItem().exerciseList.size, equalTo(2))
        }
    }

    @Test
    fun saveExerciseToRepository() = runTest {
        val viewModel = ExercisesScreenViewModel(repository)

        viewModel.saveExercise(exercise1)

        verify(repository).insertExercise(exercise1)
    }
}
