package com.askein.gymtracker.ui.exercise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class ExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val weightsExercise = ExerciseUiState(name = "Weights", muscleGroup = "Biceps", equipment = "Dumbbells")
    private val cardioExercise = ExerciseUiState(name = "Treadmill")
    private val weightsName = rule.onNode(hasText(weightsExercise.name))
    private val weightsEquipment = rule.onNode(hasText(weightsExercise.equipment))
    private val weightsMuscle = rule.onNode(hasText(weightsExercise.muscleGroup))
    private val cardioName = rule.onNode(hasText(cardioExercise.name))
    private val muscleIcon = rule.onNode(hasContentDescription("muscle icon"))
    private val dumbbellIcon = rule.onNode(hasContentDescription("equipment icon"))
    private val cardioIcon = rule.onNode(hasContentDescription("cardio icon"))

    @Test
    fun rendersWeightsExerciseCard() {
        rule.setContent {
            WeightsExerciseCard(
                exercise = weightsExercise
            )
        }

        weightsName.assertExists()
        weightsEquipment.assertExists()
        weightsMuscle.assertExists()
        muscleIcon.assertExists()
        dumbbellIcon.assertExists()
    }

    @Test
    fun rendersCardioExerciseCard() {
        rule.setContent {
            CardioExerciseCard(
                exercise = cardioExercise
            )
        }

        cardioName.assertExists()
        cardioIcon.assertExists()
    }

    @Test
    fun rendersExerciseCardWithWeightsExercise() {
        rule.setContent {
            ExerciseCard(
                exercise = weightsExercise,
                navigationFunction = {}
            )
        }

        weightsName.assertExists()
        weightsEquipment.assertExists()
        weightsMuscle.assertExists()
        muscleIcon.assertExists()
        dumbbellIcon.assertExists()
    }

    @Test
    fun rendersExerciseCardWithCardioExercise() {
        rule.setContent {
            ExerciseCard(
                exercise = cardioExercise,
                navigationFunction = {}
            )
        }

        cardioName.assertExists()
        cardioIcon.assertExists()
    }

    @Test
    fun clickingWeightsExerciseCardCallsNavigationFunction() {
        var clickedId = -1

        rule.setContent {
            ExerciseCard(
                exercise = weightsExercise,
                navigationFunction = { id -> clickedId = id }
            )
        }

        weightsName.performClick()

        assertThat(clickedId, equalTo(0))
    }

    @Test
    fun clickingCardioExerciseCardCallsNavigationFunction() {
        var clickedId = -1

        rule.setContent {
            ExerciseCard(
                exercise = cardioExercise,
                navigationFunction = { id -> clickedId = id }
            )
        }

        cardioName.performClick()

        assertThat(clickedId, equalTo(0))
    }
}