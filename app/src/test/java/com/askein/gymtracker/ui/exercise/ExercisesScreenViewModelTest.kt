package com.askein.gymtracker.ui.exercise

import app.cash.turbine.test
import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseRepository
import com.askein.gymtracker.fake.FakeExerciseRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class ExercisesScreenViewModelTest {

    private val repository: ExerciseRepository = mock()
    private val fakeRepository = FakeExerciseRepository()

    private val exercise1 = Exercise(1, "Curls", "Biceps", "Dumbbells")
    private val exercise2 = Exercise(2, "Dips", "Triceps", "Dumbbells")

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
    fun getMuscleGroupListFromRepository() = runTest {
        val viewModel = ExercisesScreenViewModel(fakeRepository)

        viewModel.muscleGroupUiState.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeRepository.emitAllMuscleGroups(listOf("Biceps", "Triceps"))

            assertThat(awaitItem().size, equalTo(2))
        }
    }

    @Test
    fun getExerciseNamesListFromRepository() = runTest {
        val viewModel = ExercisesScreenViewModel(fakeRepository)

        viewModel.exerciseNamesUiState.test {
            assertThat(awaitItem().size, equalTo(0))

            fakeRepository.emitAllExerciseNames(listOf("Curls", "Dips"))

            assertThat(awaitItem().size, equalTo(2))
        }
    }

    @Test
    fun saveExerciseToRepository() = runTest {
        val viewModel = ExercisesScreenViewModel(repository)

        viewModel.saveExercise(exercise1.toExerciseUiState())

        verify(repository).insertExercise(exercise1)
    }
}
