package com.example.gymtracker.ui.workout

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class WorkoutSelectionScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val workout1 = WorkoutUiState(1, "first")
    private val workout2 = WorkoutUiState(2, "second")

    private val screenTitle = rule.onNode(hasText("Select Workout to record"))
    private val lazyColumn = rule.onNode(hasScrollAction())
    private val firstWorkout = rule.onNode(hasText("first"))
    private val secondWorkout = rule.onNode(hasText("second"))
    private val closeButton = rule.onNode(hasContentDescription("Close"))

    @Test
    fun rendersEmptyListOfWorkouts() {
        rule.setContent {
            LiveRecordChooseWorkoutsScreen(
                workoutListUiState = WorkoutListUiState(),
                workoutNavigationFunction = { },
            )
        }

        screenTitle.assertExists()
        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(0))
    }

    @Test
    fun rendersListOfWorkouts() {
        rule.setContent {
            LiveRecordChooseWorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = listOf(
                        workout1,
                        workout2
                    )
                ),
                workoutNavigationFunction = { },
            )
        }

        screenTitle.assertExists()
        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        firstWorkout.assertExists()
        secondWorkout.assertExists()
    }

    @Test
    fun clickingWorkoutCallsWorkoutNavigationFunction() {
        var workoutChosen: Int? = null
        rule.setContent {
            LiveRecordChooseWorkoutsScreen(
                workoutListUiState = WorkoutListUiState(
                    workoutList = listOf(
                        workout1,
                        workout2
                    )
                ),
                workoutNavigationFunction = { id -> workoutChosen = id },
            )
        }

        screenTitle.assertExists()
        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        firstWorkout.assertExists()
        secondWorkout.assertExists()

        firstWorkout.performClick()

        assertThat(workoutChosen, equalTo(1))
    }

    @Test
    fun clickingCloseButtonCallsOnDismiss() {
        var dismissed = false
        rule.setContent {
            LiveRecordChooseWorkoutsScreen(
                workoutNavigationFunction = { },
                onDismiss = {dismissed = true}
            )
        }

        screenTitle.assertExists()
        lazyColumn.assertExists()
        closeButton.assertExists()

        closeButton.performClick()

        assertThat(dismissed, equalTo(true))
    }
}
