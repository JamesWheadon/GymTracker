package com.askein.gymtracker.ui.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var navController: NavHostController

    private val workouts = rule.onNode(hasText("Workouts"))
    private val exercises = rule.onNode(hasText("Exercises"))
    private val history = rule.onNode(hasText("History"))
    private val createWorkout = rule.onNode(hasContentDescription("Add Workout"))
    private val createExercise = rule.onNode(hasContentDescription("Add Exercise"))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun rendersHomeScreen() {
        rule.setContent {
            HomeScreen(
                navController = navController,
                exerciseNavigationFunction = { },
                workoutNavigationFunction = { }
            )
        }

        workouts.assertExists()
        workouts.assertIsNotEnabled()
        exercises.assertExists()
        exercises.assertIsEnabled()
        history.assertExists()
        history.assertIsEnabled()
        createWorkout.assertExists()
        createExercise.assertDoesNotExist()
    }

    @Test
    fun switchesToExerciseScreen() {
        rule.setContent {
            HomeScreen(
                navController = navController,
                exerciseNavigationFunction = { },
                workoutNavigationFunction = { }
            )
        }

        exercises.performClick()

        workouts.assertExists()
        workouts.assertIsEnabled()
        exercises.assertExists()
        exercises.assertIsNotEnabled()
        history.assertExists()
        history.assertIsEnabled()
        createWorkout.assertDoesNotExist()
        createExercise.assertExists()
    }

    @Test
    fun switchesToHistoryScreen() {
        rule.setContent {
            HomeScreen(
                navController = navController,
                exerciseNavigationFunction = { },
                workoutNavigationFunction = { }
            )
        }

        history.performClick()

        workouts.assertExists()
        workouts.assertIsEnabled()
        exercises.assertExists()
        exercises.assertIsEnabled()
        history.assertExists()
        history.assertIsNotEnabled()
        createWorkout.assertDoesNotExist()
        createExercise.assertDoesNotExist()
    }
}
