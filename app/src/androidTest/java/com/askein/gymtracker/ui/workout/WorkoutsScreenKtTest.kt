package com.askein.gymtracker.ui.workout

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.hasContentDescription
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

    private val lazyColumn = rule.onNode(hasContentDescription("workout column"))
    private val createWorkoutFormTitle = rule.onNode(hasText("Create Workout"))
    private val createWorkoutFormNameField = rule.onNode(hasContentDescription("Workout Name"))
    private val saveWorkoutButton = rule.onNode(hasText("Save"))

    private val workout1 = WorkoutUiState(1, "first")
    private val workout2 = WorkoutUiState(2, "second")
    private val firstWorkout = rule.onNode(hasText("first"))
    private val secondWorkout = rule.onNode(hasText("second"))

    @Test
    fun rendersEmptyListOfWorkouts() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(),
                showCreateWorkout = false,
                dismissCreateWorkout = { },
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
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
                showCreateWorkout = false,
                dismissCreateWorkout = { },
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
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
                showCreateWorkout = true,
                dismissCreateWorkout = { },
                createWorkout = { },
                workoutNavigationFunction = { }
            )
        }

        lazyColumn.assertExists()
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
            var show by remember { mutableStateOf(true) }
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = workoutList
                ),
                showCreateWorkout = show,
                dismissCreateWorkout = { show = false },
                createWorkout = { workout -> workoutList.add(workout) },
                workoutNavigationFunction = { }
            )
        }

        createWorkoutFormNameField.performTextInput("third")
        saveWorkoutButton.performClick()

        lazyColumn.assertExists()
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
                showCreateWorkout = false,
                dismissCreateWorkout = { },
                createWorkout = { },
                workoutNavigationFunction = { id -> workoutChosen = id }
            )
        }

        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        firstWorkout.assertExists()
        secondWorkout.assertExists()

        firstWorkout.performClick()

        assertThat(workoutChosen, equalTo(1))
    }
}
