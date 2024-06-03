package com.askein.gymtracker.ui.exercise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.askein.gymtracker.data.exercise.ExerciseType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class ExerciseScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val weightsExercise = ExerciseUiState(name = "Weights", type = ExerciseType.WEIGHTS, muscleGroup = "Biceps", equipment = "Dumbbells")
    private val cardioExercise = ExerciseUiState(name = "Treadmill", type = ExerciseType.CARDIO)
    private val calisthenicsExercise = ExerciseUiState(name = "Push ups", type = ExerciseType.CALISTHENICS)
    private val weightsName = rule.onNode(hasText(weightsExercise.name))
    private val weightsEquipment = rule.onNode(hasText(weightsExercise.equipment))
    private val weightsMuscle = rule.onNode(hasText(weightsExercise.muscleGroup))
    private val cardioName = rule.onNode(hasText(cardioExercise.name))
    private val calisthenicsName = rule.onNode(hasText(calisthenicsExercise.name))
    private val muscleIcon = rule.onNode(hasContentDescription("muscle icon"))
    private val dumbbellIcon = rule.onNode(hasContentDescription("equipment icon"))
    private val cardioIcon = rule.onNode(hasContentDescription("cardio icon"))
    private val calisthenicsIcon = rule.onNode(hasContentDescription("calisthenics icon"))

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
    fun rendersWeightsExerciseCardWithNoMuscleGroup() {
        rule.setContent {
            WeightsExerciseCard(
                exercise = ExerciseUiState(
                    name = "Weights",
                    equipment = "Dumbbells"
                )
            )
        }

        weightsName.assertExists()
        weightsEquipment.assertExists()
        weightsMuscle.assertDoesNotExist()
        muscleIcon.assertDoesNotExist()
        dumbbellIcon.assertExists()
    }

    @Test
    fun rendersWeightsExerciseCardWithNoEquipment() {
        rule.setContent {
            WeightsExerciseCard(
                exercise = ExerciseUiState(
                    name = "Weights",
                    muscleGroup = "Biceps"
                )
            )
        }

        weightsName.assertExists()
        weightsEquipment.assertDoesNotExist()
        weightsMuscle.assertExists()
        muscleIcon.assertExists()
        dumbbellIcon.assertDoesNotExist()
    }

    @Test
    fun rendersWeightsExerciseCardWithNoEquipmentAndMuscleGroup() {
        rule.setContent {
            WeightsExerciseCard(
                exercise = ExerciseUiState(
                    name = "Curls"
                )
            )
        }

        rule.onNode(hasText("Curls")).assertExists()
        weightsName.assertExists()
        weightsEquipment.assertDoesNotExist()
        weightsMuscle.assertDoesNotExist()
        muscleIcon.assertDoesNotExist()
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
    fun rendersCalisthenicsExerciseCard() {
        rule.setContent {
            CalisthenicsExerciseCard(
                exercise = calisthenicsExercise
            )
        }

        calisthenicsName.assertExists()
        calisthenicsIcon.assertExists()
    }

    @Test
    fun rendersCalisthenicsExerciseCardWithMuscleGroup() {
        rule.setContent {
            CalisthenicsExerciseCard(
                exercise = ExerciseUiState(name = "Push ups", type = ExerciseType.CALISTHENICS, muscleGroup = "Chest")
            )
        }

        calisthenicsName.assertExists()
        muscleIcon.assertExists()
        rule.onNode(hasText("Chest")).assertExists()
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
    fun rendersExerciseCardWithCalisthenicsExercise() {
        rule.setContent {
            ExerciseCard(
                exercise = calisthenicsExercise,
                navigationFunction = { _, _ -> (Unit) }
            )
        }

        calisthenicsName.assertExists()
        calisthenicsIcon.assertExists()
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
    fun clickingCalisthenicsExerciseCardCallsNavigationFunction() {
        var clickedId = -1
        var chosenDate = LocalDate.now()

        rule.setContent {
            ExerciseCard(
                exercise = calisthenicsExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                }
            )
        }

        calisthenicsName.performClick()

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

    @Test
    fun clickingCalisthenicsExerciseCardCallsNavigationFunctionWithDate() {
        var clickedId = -1
        var chosenDate: LocalDate? = null

        rule.setContent {
            ExerciseCard(
                exercise = calisthenicsExercise,
                navigationFunction = { id, date ->
                    clickedId = id
                    chosenDate = date
                },
                chosenDate = LocalDate.now()
            )
        }

        calisthenicsName.performClick()

        assertThat(clickedId, equalTo(0))
        assertThat(chosenDate, equalTo(LocalDate.now()))
    }
}