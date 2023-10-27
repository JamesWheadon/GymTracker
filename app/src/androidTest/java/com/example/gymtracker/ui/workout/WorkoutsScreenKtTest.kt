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
import org.hamcrest.MatcherAssert
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

    @Test
    fun rendersEmptyListOfWorkouts() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(),
                createWorkout = { }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        MatcherAssert.assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(0))
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
                createWorkout = { }
            )
        }

        lazyColumn.assertExists()
        createWorkoutButton.assertExists()
        MatcherAssert.assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        rule.onNode(hasText("first")).assertExists()
        rule.onNode(hasText("second")).assertExists()
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
                createWorkout = { }
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
                createWorkout = { workout -> workoutList.add(workout.toWorkoutUiState()) }
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
        MatcherAssert.assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(3))
        rule.onNode(hasText("first")).assertExists()
        rule.onNode(hasText("second")).assertExists()
        rule.onNode(hasText("third")).assertExists()
    }
}
