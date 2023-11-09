package com.example.gymtracker.ui.workout.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.workout.WorkoutWithExercisesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class WorkoutDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var navController: NavHostController

    private val workoutNoExercises = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = "Test Workout"
    )
    private val curlsExerciseUiState = ExerciseUiState(0, "Curls", "Biceps", "Dumbbells")
    private val dipsExerciseUiState = ExerciseUiState(1, "Dips", "Triceps", "Dumbbells And Bars")
    private val workoutWithExercises = WorkoutWithExercisesUiState(
        workoutId = 1,
        name = "Test Workout",
        exercises = listOf(
            curlsExerciseUiState,
            dipsExerciseUiState
        )
    )

    private val workoutTitle = rule.onNode(hasText("Test Workout"))
    private val curlsExercise = rule.onNode(hasText("Curls"))
    private val dipsExercise = rule.onNode(hasText("Dips"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun rendersWorkoutWithNoExercises() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutNoExercises,
                navController = navController,
                exerciseNavigationFunction = { },
            )
        }

        workoutTitle.assertExists()
    }

    @Test
    fun rendersWorkoutWithExercises() {
        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { },
            )
        }

        workoutTitle.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()
    }

    @Test
    fun clickingExerciseCallsExerciseNavigationFunctionWithId() {
        var calledExerciseId: Int? = null

        rule.setContent {
            WorkoutDetailsScreen(
                uiState = workoutWithExercises,
                navController = navController,
                exerciseNavigationFunction = { id -> calledExerciseId = id },
            )
        }

        workoutTitle.assertExists()
        curlsExercise.assertExists()
        dipsExercise.assertExists()

        curlsExercise.performClick()

        assertThat(calledExerciseId, equalTo(0))
    }
}
