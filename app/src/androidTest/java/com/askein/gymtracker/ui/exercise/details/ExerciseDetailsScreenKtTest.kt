package com.askein.gymtracker.ui.exercise.details

import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.askein.gymtracker.ui.exercise.ExerciseUiState
import com.askein.gymtracker.ui.user.LocalUserPreferences
import com.askein.gymtracker.ui.user.UserPreferencesUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

private const val NAME = "Curls"
private const val MUSCLE_GROUP = "Biceps"
private const val EQUIPMENT = "Dumbbells"

class ExerciseDetailsScreenKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var navController: NavHostController

    private val exercise = ExerciseDetailsUiState(
        exercise = ExerciseUiState(
            name = NAME,
            muscleGroup = MUSCLE_GROUP,
            equipment = EQUIPMENT
        )
    )

    private val exerciseName = rule.onNode(hasText(NAME) and hasContentDescription("Exercise Name").not())
    private val addHistoryButton = rule.onNode(hasContentDescription("Record exercise"))
    private val newHistoryTitle = rule.onNode(hasText("New $NAME Workout"))
    private val editExerciseButton = rule.onNode(hasContentDescription("edit feature"))
    private val updateExerciseTitle = rule.onNode(hasText("Update Exercise"))
    private val deleteExerciseButton = rule.onNode(hasContentDescription("delete feature"))
    private val deleteExerciseTitle = rule.onNode(hasText("Delete $NAME Exercise?"))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun rendersExerciseDetailsWithNoHistory() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                navController = navController,
                updateFunction = { },
                deleteFunction = { },
                chosenDate = null
            )
        }

        exerciseName.assertExists()
        addHistoryButton.assertExists()
    }

    @Test
    fun rendersRecordExerciseHistoryDialog() {
        rule.setContent {
            val userPreferencesUiState = UserPreferencesUiState()
            CompositionLocalProvider(LocalUserPreferences provides userPreferencesUiState) {
                ExerciseDetailsScreen(
                    uiState = exercise,
                    navController = navController,
                    updateFunction = { },
                    deleteFunction = { },
                    chosenDate = null
                )
            }
        }

        newHistoryTitle.assertDoesNotExist()

        addHistoryButton.performClick()

        newHistoryTitle.assertExists()
    }

    @Test
    fun rendersEditExerciseDialog() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                navController = navController,
                updateFunction = { },
                deleteFunction = { },
                chosenDate = null
            )
        }

        updateExerciseTitle.assertDoesNotExist()

        editExerciseButton.performClick()

        updateExerciseTitle.assertExists()
        exerciseName.assertExists()
    }

    @Test
    fun rendersDeleteExerciseDialog() {
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                navController = navController,
                updateFunction = { },
                deleteFunction = { },
                chosenDate = null
            )
        }

        deleteExerciseTitle.assertDoesNotExist()

        deleteExerciseButton.performClick()

        deleteExerciseTitle.assertExists()
        exerciseName.assertExists()
    }

    @Test
    fun deleteExerciseDialogYesClickedDeleteAndBackFunctionsCalled() {
        var updateCalled = false
        var deleteCalled = false
        rule.setContent {
            ExerciseDetailsScreen(
                uiState = exercise,
                navController = navController,
                updateFunction = { updateCalled = true },
                deleteFunction = { deleteCalled = true },
                chosenDate = null
            )
        }

        deleteExerciseButton.performClick()
        rule.onNode(hasText("Yes")).performClick()

        assertThat(updateCalled, equalTo(false))
        assertThat(deleteCalled, equalTo(true))
        verify(navController).popBackStack()
    }
}
