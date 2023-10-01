package com.example.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
import com.example.gymtracker.ui.history.ExerciseHistoryUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

private const val NAME = "Curls"
private const val MUSCLE_GROUP = "Biceps"
private const val EQUIPMENT = "Dumbbells"

class ExerciseDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val exerciseNoHistory = ExerciseDetailsUiState(
        name = NAME,
        muscleGroup = MUSCLE_GROUP,
        equipment = EQUIPMENT,
        history = listOf()
    )
    private val exercise = ExerciseDetailsUiState(
        name = NAME,
        muscleGroup = MUSCLE_GROUP,
        equipment = EQUIPMENT,
        history = listOf(
            ExerciseHistoryUiState(
                id = 1,
                weight = 13.0,
                sets = 1,
                reps = 2,
                rest = 1,
                date = LocalDate.now().minusDays(5)
            )
        )
    )

    private val exerciseName = rule.onNode(hasText(NAME) and hasContentDescription("Exercise Name").not())
    private val exerciseEquipment = rule.onNode(hasText(EQUIPMENT) and hasContentDescription("Equipment").not())
    private val exerciseMuscleGroup = rule.onNode(hasText(MUSCLE_GROUP) and hasContentDescription("Muscle Group").not())
    private val exerciseMuscleGroupIcon = rule.onNode(hasContentDescription("exercise icon"))
    private val exerciseEquipmentIcon = rule.onNode(hasContentDescription("equipment icon"))
    private val bestExerciseIcon = rule.onNode(hasContentDescription("best exercise icon"))
    private val recentExerciseIcon = rule.onNode(hasContentDescription("recent exercise icon"))
    private val addHistoryButton = rule.onNode(hasContentDescription("Add Workout"))
    private val newHistoryTitle = rule.onNode(hasText("New $NAME Workout"))
    private val editExerciseButton = rule.onNode(hasContentDescription("Edit feature"))
    private val updateExerciseTitle = rule.onNode(hasText("Update Exercise"))

    @Test
    fun rendersExerciseDetailsWithNoHistory() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exerciseNoHistory,
                backNavigationFunction = { },
                updateFunction = { }
            )
        }

        exerciseName.assertExists()
        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        addHistoryButton.assertExists()
        bestExerciseIcon.assertDoesNotExist()
        recentExerciseIcon.assertDoesNotExist()
    }

    @Test
    fun rendersExerciseDetailsWithHistory() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                backNavigationFunction = { },
                updateFunction = { }
            )
        }

        exerciseName.assertExists()
        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        addHistoryButton.assertExists()
        bestExerciseIcon.assertExists()
        recentExerciseIcon.assertExists()
    }

    @Test
    fun rendersRecordExerciseHistoryDialog() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                backNavigationFunction = { },
                updateFunction = { }
            )
        }

        newHistoryTitle.assertDoesNotExist()

        addHistoryButton.performClick()

        newHistoryTitle.assertExists()
        exerciseName.assertExists()
        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        bestExerciseIcon.assertExists()
        recentExerciseIcon.assertExists()
    }

    @Test
    fun rendersEditExerciseDialog() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                backNavigationFunction = { },
                updateFunction = { }
            )
        }

        updateExerciseTitle.assertDoesNotExist()

        editExerciseButton.performClick()

        updateExerciseTitle.assertExists()
        exerciseName.assertExists()
        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        bestExerciseIcon.assertExists()
        recentExerciseIcon.assertExists()
    }
}