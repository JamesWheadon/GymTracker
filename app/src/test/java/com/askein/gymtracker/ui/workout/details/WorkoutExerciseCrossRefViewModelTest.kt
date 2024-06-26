package com.askein.gymtracker.ui.workout.details

import com.askein.gymtracker.data.exercise.Exercise
import com.askein.gymtracker.data.exercise.ExerciseType
import com.askein.gymtracker.data.workout.Workout
import com.askein.gymtracker.data.workoutExerciseCrossRef.WorkoutExerciseCrossRefRepository
import com.askein.gymtracker.rules.TestCoroutineRule
import com.askein.gymtracker.ui.exercise.toExerciseUiState
import com.askein.gymtracker.ui.workout.toWorkoutUiState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class WorkoutExerciseCrossRefViewModelTest {

    private val mockRepository: WorkoutExerciseCrossRefRepository = mock()
    private val exercise = Exercise(
        exerciseId = 1,
        exerciseType = ExerciseType.WEIGHTS,
        name = "test name",
        muscleGroup = "test muscle",
        equipment = "test kit"
    )
    private val workout = Workout(workoutId = 1, name = "test workout")
    private lateinit var viewModel: WorkoutExerciseCrossRefViewModel

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Test
    fun insertWorkoutExerciseCrossRefInToRepository() = runTest {
        viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

        viewModel.saveExerciseToWorkout(exercise.toExerciseUiState(), workout.toWorkoutUiState())

        verify(mockRepository).saveExerciseToWorkout(exercise, workout)
    }

    @Test
    fun deleteWorkoutExerciseCrossRefFromRepository() = runTest {
        viewModel = WorkoutExerciseCrossRefViewModel(mockRepository)

        viewModel.deleteExerciseFromWorkout(
            exercise.toExerciseUiState(),
            workout.toWorkoutUiState()
        )

        verify(mockRepository).deleteExerciseFromWorkout(exercise, workout)
    }
}