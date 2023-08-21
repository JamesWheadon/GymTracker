//package com.example.gymtracker.ui.history
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.gymtracker.data.history.ExerciseHistory
//import com.example.gymtracker.ui.AppViewModelProvider
//import com.example.gymtracker.ui.exercise.DropdownBox
//import com.example.gymtracker.ui.exercise.ExerciseInformationField
//import com.example.gymtracker.ui.exercise.ExerciseUiState
//import com.example.gymtracker.ui.exercise.ExerciseViewModel
//import com.example.gymtracker.ui.theme.GymTrackerTheme
//import java.time.LocalDate
//
//
//@Composable
//fun RecordExerciseScreen(
//    modifier: Modifier = Modifier,
//    viewModel: ExerciseViewModel = viewModel(
//        factory = AppViewModelProvider.Factory
//    )
//) {
//    val uiState = viewModel.getExerciseStream(1).collectAsState(initial = null).value!!
//    RecordExerciseScreen(
//        exercise = uiState,
//        saveFunction = { history ->
//            viewModel.saveHistory(history)
//        },
//        modifier = modifier
//    )
//}
//
//@Composable
//fun RecordExerciseScreen(
//    exercise: ExerciseUiState,
//    saveFunction: (ExerciseHistory) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val customCardElevation = CardDefaults.cardElevation(
//        defaultElevation = 16.dp
//    )
//    var setsState by remember { mutableStateOf("") }
//    var repsState by remember { mutableStateOf("") }
//    var weightState by remember { mutableStateOf("") }
//    var unitState by remember { mutableStateOf("kg") }
//    Card(
//        modifier = modifier
//            .background(MaterialTheme.colorScheme.background)
//            .padding(vertical = 10.dp, horizontal = 10.dp),
//        elevation = customCardElevation
//    ) {
//        Column {
//            Column(
//                modifier = modifier
//                    .fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Spacer(modifier = Modifier.height(6.dp))
//                Text(
//                    text = "New ${exercise.name} Workout"
//                )
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 12.dp, vertical = 0.dp)
//                ) {
//                    ExerciseInformationField(
//                        label = "Sets",
//                        value = setsState,
//                        onChange = { entry ->
//                            setsState = entry
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(0.dp)
//                    )
//                    Spacer(modifier = Modifier.width(12.dp))
//                    ExerciseInformationField(
//                        label = "Reps",
//                        value = repsState,
//                        onChange = { entry ->
//                            repsState = entry
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(0.dp)
//                    )
//                }
//                Row(
//                    horizontalArrangement = Arrangement.SpaceEvenly,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 12.dp, vertical = 0.dp)
//                ) {
//                    ExerciseInformationField(
//                        label = "Weight",
//                        value = weightState,
//                        onChange = { entry ->
//                            weightState = entry
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(0.dp)
//                    )
//                    Spacer(modifier = Modifier.width(12.dp))
//                    DropdownBox(
//                        options = listOf("kg", "lb"),
//                        onChange = { value ->
//                            unitState = value
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(0.dp)
//                    )
//                }
//                if (setsState != "" && repsState != "" && weightState != "" && unitState != "") {
//                    Button(onClick = {
//                        var weight = weightState.toDouble()
//                        if (unitState == "lb") {
//                            weight *= 2.2
//                        }
//                        val history = ExerciseHistory(
//                            exerciseId = exercise.id,
//                            weight = weight,
//                            sets = setsState.toInt(),
//                            reps = repsState.toInt(),
//                            date = LocalDate.now()
//                        )
//                        saveFunction(history)
//                    }) {
//                        Text("Save")
//                    }
//                } else {
//                    Button(
//                        onClick = { },
//                        enabled = false
//                    ) {
//                        Text("Save")
//                    }
//                }
//                Spacer(modifier = Modifier.height(6.dp))
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ExerciseScreenPreview() {
//    GymTrackerTheme(darkTheme = false) {
//        RecordExerciseScreen(
//            exercise = ExerciseUiState(
//                name = "Curls",
//                muscleGroup = "Biceps",
//                equipment = "Dumbbells"
//            ), saveFunction = {}
//        )
//    }
//}
