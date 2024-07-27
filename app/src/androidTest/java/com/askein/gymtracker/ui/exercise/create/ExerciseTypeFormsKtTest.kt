package com.askein.gymtracker.ui.exercise.create

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performTextInput
import com.askein.gymtracker.data.exercise.ExerciseType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class ExerciseTypeFormsKtTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val emptyExerciseInfo = ExerciseInfo(
        exerciseId = 0,
        exerciseType = ExerciseType.WEIGHTS,
        name = "",
        equipment = "",
        muscleGroup = ""
    )

    private val populatedExerciseInfo = ExerciseInfo(
        exerciseId = 0,
        exerciseType = ExerciseType.WEIGHTS,
        name = "Name",
        equipment = "Kit",
        muscleGroup = "Muscle"
    )

    private val nameField = rule.onNode(hasContentDescription("Exercise Name"))
    private val equipmentField = rule.onNode(hasContentDescription("Equipment"))
    private val muscleField = rule.onNode(hasContentDescription("Muscle Group"))
    private val nameError = rule.onNode(hasText("Name already taken"))

    @Test
    fun rendersEmptyWeightsExerciseForm() {
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertExists()
        nameError.assertDoesNotExist()
        equipmentField.assertExists()
        muscleField.assertExists()
    }

    @Test
    fun rendersWeightsExerciseFormWithNameError() {
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = true,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertExists()
        nameError.assertExists()
        equipmentField.assertExists()
        muscleField.assertExists()
    }

    @Test
    fun rendersWeightsExerciseFormWithExistingExerciseInfo() {
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = populatedExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertTextContains(populatedExerciseInfo.name)
        equipmentField.assertTextContains(populatedExerciseInfo.equipment)
        muscleField.assertTextContains(populatedExerciseInfo.muscleGroup)
    }

    @Test
    fun updatingNameFieldInWeightsExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.performTextInput("Name")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(name = "Name")))
    }

    @Test
    fun updatingEquipmentFieldInWeightsExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        equipmentField.performTextInput("Kit")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(equipment = "Kit")))
    }

    @Test
    fun updatingMuscleFieldInWeightsExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            WeightsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        muscleField.performTextInput("Muscle")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(muscleGroup = "Muscle")))
    }


    @Test
    fun rendersEmptyCardioExerciseForm() {
        rule.setContent {
            CardioExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false
            )
        }

        nameField.assertExists()
        nameError.assertDoesNotExist()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
    }

    @Test
    fun rendersCardioExerciseFormWithNameError() {
        rule.setContent {
            CardioExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = true
            )
        }

        nameField.assertExists()
        nameError.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertDoesNotExist()
    }

    @Test
    fun rendersCardioExerciseFormWithExistingExerciseInfo() {
        rule.setContent {
            CardioExerciseForm(
                exerciseInfo = populatedExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false
            )
        }

        nameField.assertTextContains(populatedExerciseInfo.name)
    }

    @Test
    fun updatingNameFieldInCardioExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            CardioExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false
            )
        }

        nameField.performTextInput("Name")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(name = "Name")))
    }
    @Test
    fun rendersEmptyCalisthenicsExerciseForm() {
        rule.setContent {
            CalisthenicsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertExists()
        nameError.assertDoesNotExist()
        equipmentField.assertDoesNotExist()
        muscleField.assertExists()
    }

    @Test
    fun rendersCalisthenicsExerciseFormWithNameError() {
        rule.setContent {
            CalisthenicsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = true,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertExists()
        nameError.assertExists()
        equipmentField.assertDoesNotExist()
        muscleField.assertExists()
    }

    @Test
    fun rendersCalisthenicsExerciseFormWithExistingExerciseInfo() {
        rule.setContent {
            CalisthenicsExerciseForm(
                exerciseInfo = populatedExerciseInfo,
                exerciseInfoOnChange = { },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.assertTextContains(populatedExerciseInfo.name)
        muscleField.assertTextContains(populatedExerciseInfo.muscleGroup)
    }

    @Test
    fun updatingNameFieldInCalisthenicsExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            CalisthenicsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        nameField.performTextInput("Name")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(name = "Name")))
    }

    @Test
    fun updatingMuscleFieldInCalisthenicsExerciseFormUpdatesExerciseInfo() {
        var updateExerciseInfo: ExerciseInfo? = null
        rule.setContent {
            CalisthenicsExerciseForm(
                exerciseInfo = emptyExerciseInfo,
                exerciseInfoOnChange = { newValue -> updateExerciseInfo = newValue },
                nameError = false,
                savedMuscleGroups = listOf()
            )
        }

        muscleField.performTextInput("Muscle")
        assertThat(updateExerciseInfo, equalTo(emptyExerciseInfo.copy(muscleGroup = "Muscle")))
    }
}
