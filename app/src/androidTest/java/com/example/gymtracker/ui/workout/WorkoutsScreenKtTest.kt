package com.example.gymtracker.ui.workout

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class WorkoutsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val lazyColumn = rule.onNode(hasScrollAction())
    private val createWorkoutButton = rule.onNode(hasContentDescription("Add Workout"))
    private val createWorkoutFormTitle = rule.onNode(hasText("Create Workout"))
    private val createWorkoutFormNameField = rule.onNode(hasContentDescription("Workout Name"))
    private val saveWorkoutButton = rule.onNode(hasText("Save Workout"))

    private val workout1 = WorkoutUiState(1, "first")
    private val workout2 = WorkoutUiState(2, "second")

    private val firstWorkout = rule.onNode(hasText("first"))
    private val secondWorkout = rule.onNode(hasText("first"))

    @Test
    fun rendersEmptyListOfWorkouts() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(),
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(0))
    }

    @Test
    fun rendersListOfWorkouts() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = listOf(
                        workout1,
                        workout2
                    )
                ),
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        firstWorkout.assertExists()
        secondWorkout.assertExists()
    }

    @Test
    fun clickingPlusButtonOpensCreateWorkoutFormDialog() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = listOf(
                        workout1,
                        workout2
                    )
                ),
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        createWorkoutFormTitle.assertDoesNotExist()
        createWorkoutFormNameField.assertDoesNotExist()
        saveWorkoutButton.assertDoesNotExist()

        createWorkoutButton.performClick()

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        createWorkoutFormTitle.assertExists()
        createWorkoutFormNameField.assertExists()
        saveWorkoutButton.assertExists()
    }

    @Test
    fun enteringWorkoutNameThenClickingSaveAddsWorkoutToList() {
        val workoutList = mutableListOf(
            workout1,
            workout2
        )
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = workoutList
                ),
                createWorkout = { workout -> workoutList.add(workout.toWorkoutUiState()) },
                workoutNavigationFunction = { }
            )
        }

        createWorkoutButton.performClick()

        createWorkoutFormNameField.performTextInput("third")
        saveWorkoutButton.performClick()

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        createWorkoutFormTitle.assertDoesNotExist()
        createWorkoutFormNameField.assertDoesNotExist()
        saveWorkoutButton.assertDoesNotExist()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(3))
        firstWorkout.assertExists()
        secondWorkout.assertExists()
        rule.onNode(hasText("third")).assertExists()
    }

    @Test
    fun clickingWorkoutCallsWorkoutNavigationFunction() {
        var workoutChosen: Int? = null
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = listOf(
                        workout1,
                        workout2
                    )
                ),
                createWorkout = { },
                workoutNavigationFunction = { id -> workoutChosen = id }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        firstWorkout.assertExists()
        secondWorkout.assertExists()

        firstWorkout.performClick()

        assertThat(workoutChosen, equalTo(1))
    }
}