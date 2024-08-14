package com.askein.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

private const val NAME = "Curls"
private const val MUSCLE_GROUP = "Biceps"
private const val EQUIPMENT = "Dumbbells"

class WeightsExerciseDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val exerciseNoHistory = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME,
            muscleGroup = MUSCLE_GROUP,
            equipment = EQUIPMENT
        )
    )
    private val exercise = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME,
            muscleGroup = MUSCLE_GROUP,
            equipment = EQUIPMENT
        ),
        weightsHistory = listOf(
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(13.0),
                sets = 1,
                reps = listOf(2),
                rest = 1,
                date = LocalDate.now().minusDays(5)
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(12.0),
                sets = 1,
                reps = listOf(5),
                rest = 1,
                date = LocalDate.now().minusDays(3)
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = listOf(10.0),
                sets = 1,
                reps = listOf(5),
                rest = 1,
                date = LocalDate.now()
            )
        )
    )

    private val exerciseEquipment =
        rule.onNode(hasText(EQUIPMENT) and hasContentDescription("Equipment").not())
    private val exerciseMuscleGroup =
        rule.onNode(hasText(MUSCLE_GROUP) and hasContentDescription("Muscle Group").not())
    private val exerciseMuscleGroupIcon = rule.onNode(hasContentDescription("muscle icon"))
    private val exerciseEquipmentIcon = rule.onNode(hasContentDescription("equipment icon"))
    private val bestExerciseIcon = rule.onNode(hasContentDescription("best exercise icon"))
    private val bestExercise = rule.onNode(hasText("13.0 kg for 2 reps"))
    private val bestExerciseMostWeight = rule.onNode(hasText("26.5 lb for 5 reps"))
    private val recentExerciseIcon = rule.onNode(hasContentDescription("recent exercise icon"))
    private val recentExercise = rule.onNode(hasText("10.0 kg for 5 reps"))
    private val recentExercisePounds = rule.onNode(hasText("22.1 lb for 5 reps"))


    @Test
    fun rendersExerciseDetailsWithNoHistory() {
        rule.setContent {
            WeightsExerciseDetailsScreen(
                innerPadding = PaddingValues(0.dp),
                uiState = exerciseNoHistory,
                chosenDate = null
            )
        }

        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        bestExerciseIcon.assertDoesNotExist()
        recentExerciseIcon.assertDoesNotExist()
    }

    @Test
    fun rendersExerciseDetailsWithHistory() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WeightsExerciseDetailsScreen(
                    innerPadding = PaddingValues(0.dp),
                    uiState = exercise,
                    chosenDate = null
                )
            }
        }

        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        bestExerciseIcon.assertExists()
        recentExerciseIcon.assertExists()
        bestExercise.assertExists()
        recentExercise.assertExists()
    }

    @Test
    fun rendersExerciseDetailsWithPoundsWeightAndHighestTotalSetWeight() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState(
                displayHighestWeight = false,
                defaultWeightUnit = WeightUnits.POUNDS
            )
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                WeightsExerciseDetailsScreen(
                    innerPadding = PaddingValues(0.dp),
                    uiState = exercise,
                    chosenDate = null
                )
            }
        }

        exerciseMuscleGroup.assertExists()
        exerciseMuscleGroupIcon.assertExists()
        exerciseEquipment.assertExists()
        exerciseEquipmentIcon.assertExists()
        bestExerciseIcon.assertExists()
        recentExerciseIcon.assertExists()
        bestExerciseMostWeight.assertExists()
        recentExercisePounds.assertExists()
    }
}
