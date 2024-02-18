package com.example.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
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
                weight = 13.0,
                sets = 1,
                reps = 2,
                rest = 1,
                date = LocalDate.now().minusDays(5)
            ),
            WeightsExerciseHistoryUiState(
                id = 1,
                weight = 10.0,
                sets = 1,
                reps = 5,
                rest = 1,
                date = LocalDate.now()
            )
        )
    )

    private val exerciseEquipment =
        rule.onNode(hasText(EQUIPMENT) and hasContentDescription("Equipment").not())
    private val exerciseMuscleGroup =
        rule.onNode(hasText(MUSCLE_GROUP) and hasContentDescription("Muscle Group").not())
    private val exerciseMuscleGroupIcon = rule.onNode(hasContentDescription("exercise icon"))
    private val exerciseEquipmentIcon = rule.onNode(hasContentDescription("equipment icon"))
    private val bestExerciseIcon = rule.onNode(hasContentDescription("best exercise icon"))
    private val bestExercise = rule.onNode(hasText("13.0 kg for 2 reps"))
    private val recentExerciseIcon = rule.onNode(hasContentDescription("recent exercise icon"))
    private val recentExercise = rule.onNode(hasText("10.0 kg for 5 reps"))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun rendersExerciseDetailsWithNoHistory() {
        rule.setContent {
            WeightsExerciseDetailsScreen(
                uiState = exerciseNoHistory,
                innerPadding = PaddingValues(0.dp)
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
            WeightsExerciseDetailsScreen(
                uiState = exercise,
                innerPadding = PaddingValues(0.dp)
            )
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
}
