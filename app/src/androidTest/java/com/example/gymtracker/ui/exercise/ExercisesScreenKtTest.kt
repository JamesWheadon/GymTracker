package com.example.gymtracker.ui.exercise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.performClick
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class ExercisesScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val lazyColumn = rule.onNode(hasScrollAction())
    private val createButton = rule.onNode(hasContentDescription("Add Exercise"))

    private val exercise1 = ExerciseUiState(1, "Curls", "Biceps", "Dumbbells")
    private val exercise2 = ExerciseUiState(2, "Curls", "Biceps", "Dumbbells")

    @Test
    fun rendersEmptyListOfExercises() {
        rule.setContent {
            ExerciseScreen(
                exerciseNavigationFunction = {},
                exerciseListUiState = ExerciseListUiState(),
                homeNavigationOptions = mapOf()
            )
        }

        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(0))
    }

    @Test
    fun rendersListOfExercises() {
        rule.setContent {
            ExerciseScreen(
                exerciseNavigationFunction = {},
                exerciseListUiState = ExerciseListUiState(
                    exerciseList = listOf(
                        exercise1,
                        exercise2
                    )
                ),
                homeNavigationOptions = mapOf()
            )
        }

        lazyColumn.assertExists()
        assertThat(lazyColumn.onChildren().fetchSemanticsNodes().size, equalTo(2))
    }

    @Test
    fun clickExerciseToNavigateToDetails() {
        val exerciseId = 1
        var navigateId = 0
        rule.setContent {
            ExerciseScreen(
                exerciseNavigationFunction = { navigateId = it },
                exerciseListUiState = ExerciseListUiState(
                    exerciseList = listOf(
                        exercise1
                    )
                ),
                homeNavigationOptions = mapOf()
            )
        }

        val exerciseCard = rule.onNode(hasClickAction() and hasParent(hasScrollAction()))
        exerciseCard.performClick()
        assertThat(navigateId, equalTo(exerciseId))
    }

    @Test
    fun clickPlusToOpenDialog() {
        rule.setContent {
            ExerciseScreen(
                exerciseNavigationFunction = {},
                exerciseListUiState = ExerciseListUiState(
                    exerciseList = listOf(
                        exercise1,
                        exercise2
                    )
                ),
                homeNavigationOptions = mapOf()
            )
        }

        rule.onNode(hasText("Create New Exercise")).assertDoesNotExist()
        createButton.performClick()
        rule.onNode(hasText("Create New Exercise")).assertExists()
    }
}
