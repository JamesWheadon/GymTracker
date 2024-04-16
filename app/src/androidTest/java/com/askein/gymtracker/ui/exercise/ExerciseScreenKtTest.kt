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
import java.time.LocalDate

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
                navigationFunction = { _, _ -> (Unit) }
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
                navigationFunction = { _, _ -> (Unit) }
            )
        }

        cardioName.assertExists()
        cardioIcon.assertExists()
    }

    @Test
    fun clickingWeightsExerciseCardCallsNavigationFunction() {
        var clickedId = -1
        var chosenDate = LocalDate.now()

        rule.setContent {
            ExerciseCard(
                exercise = weightsExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                }
            )
        }

        weightsName.performClick()

        assertThat(clickedId, equalTo(0))
        assertThat(chosenDate, equalTo(null))
    }

    @Test
    fun clickingCardioExerciseCardCallsNavigationFunction() {
        var clickedId = -1
        var chosenDate = LocalDate.now()

        rule.setContent {
            ExerciseCard(
                exercise = cardioExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                }
            )
        }

        cardioName.performClick()

        assertThat(clickedId, equalTo(0))
        assertThat(chosenDate, equalTo(null))
    }

    @Test
    fun clickingWeightsExerciseCardCallsNavigationFunctionWithDate() {
        var clickedId = -1
        var chosenDate: LocalDate? = null

        rule.setContent {
            ExerciseCard(
                exercise = weightsExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                },
                chosenDate = LocalDate.now()
            )
        }

        weightsName.performClick()

        assertThat(clickedId, equalTo(0))
        assertThat(chosenDate, equalTo(LocalDate.now()))
    }

    @Test
    fun clickingCardioExerciseCardCallsNavigationFunctionWithDate() {
        var clickedId = -1
        var chosenDate: LocalDate? = null

        rule.setContent {
            ExerciseCard(
                exercise = cardioExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                },
                chosenDate = LocalDate.now()
            )
        }

        cardioName.performClick()

        assertThat(clickedId, equalTo(0))
        assertThat(chosenDate, equalTo(LocalDate.now()))
    }
}