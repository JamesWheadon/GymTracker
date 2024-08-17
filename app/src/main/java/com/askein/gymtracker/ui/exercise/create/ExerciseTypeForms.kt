package com.askein.gymtracker.ui.exercise.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.enums.FormTypes
import com.askein.gymtracker.ui.FormInformationField
import com.askein.gymtracker.ui.FormInformationFieldWithSuggestions

@Composable
fun WeightsExerciseForm(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    nameError: Boolean,
    savedMuscleGroups: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExerciseNameFormField(
            exerciseInfo = exerciseInfo,
            exerciseInfoOnChange = exerciseInfoOnChange,
            nameError = nameError
        )
        FormInformationField(
            label = R.string.equipment,
            value = exerciseInfo.equipment,
            onChange = { newEquip -> exerciseInfoOnChange(exerciseInfo.copy(equipment = newEquip)) },
            formType = FormTypes.STRING,
        )
        ExerciseMuscleGroupFormField(
            exerciseInfo = exerciseInfo,
            exerciseInfoOnChange = exerciseInfoOnChange,
            savedMuscleGroups = savedMuscleGroups
        )
    }
}

@Composable
fun CardioExerciseForm(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    nameError: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExerciseNameFormField(
            exerciseInfo = exerciseInfo,
            exerciseInfoOnChange = exerciseInfoOnChange,
            nameError = nameError
        )
    }
}

@Composable
fun CalisthenicsExerciseForm(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    nameError: Boolean,
    savedMuscleGroups: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExerciseNameFormField(
            exerciseInfo = exerciseInfo,
            exerciseInfoOnChange = exerciseInfoOnChange,
            nameError = nameError
        )
        ExerciseMuscleGroupFormField(
            exerciseInfo = exerciseInfo,
            exerciseInfoOnChange = exerciseInfoOnChange,
            savedMuscleGroups = savedMuscleGroups
        )
    }
}

@Composable
private fun ExerciseNameFormField(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    nameError: Boolean
) {
    FormInformationField(
        label = R.string.exercise_name,
        value = exerciseInfo.name,
        onChange = { newName -> exerciseInfoOnChange(exerciseInfo.copy(name = newName)) },
        formType = FormTypes.STRING,
        error = nameError,
        errorMessage = R.string.exercise_name_taken
    )
}

@Composable
private fun ExerciseMuscleGroupFormField(
    exerciseInfo: ExerciseInfo,
    exerciseInfoOnChange: (ExerciseInfo) -> Unit,
    savedMuscleGroups: List<String>
) {
    FormInformationFieldWithSuggestions(
        label = R.string.muscle_group,
        value = exerciseInfo.muscleGroup,
        onChange = { newMuscle -> exerciseInfoOnChange(exerciseInfo.copy(muscleGroup = newMuscle)) },
        suggestions = savedMuscleGroups
    )
}
