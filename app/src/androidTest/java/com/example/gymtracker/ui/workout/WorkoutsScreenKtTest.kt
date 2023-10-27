package com.example.gymtracker.ui.workout

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class WorkoutsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val lazyColumn = rule.onNode(hasScrollAction())

    private val workout1 = WorkoutUiState(1, "first")
    private val workout2 = WorkoutUiState(2, "second")

    @Test
    fun rendersEmptyListOfWorkouts() {
        rule.setContent {
            WorkoutsScreen(
                workoutListUiState = WorkoutListUiState()
            )
        }

        lazyColumn.assertExists()
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
                )
            )
        }

        lazyColumn.assertExists()
        MatcherAssert.assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
        rule.onNode(hasText("first")).assertExists()
        rule.onNode(hasText("second")).assertExists()
    }
}
