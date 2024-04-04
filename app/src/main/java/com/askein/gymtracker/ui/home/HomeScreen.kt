package com.askein.gymtracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.exercise.ExercisesScreen
import com.askein.gymtracker.ui.history.OverallHistoryScreen
import com.askein.gymtracker.ui.navigation.NavigationRoute
import com.askein.gymtracker.ui.navigation.NavigationRoutes
import com.askein.gymtracker.ui.navigation.TopBar
import com.askein.gymtracker.ui.workout.WorkoutsScreen

object HomeScreenRoute : NavigationRoute {
    override val route = NavigationRoutes.HOME_SCREEN.baseRoute
}

@Composable
fun HomeScreen(
    navController: NavHostController,
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateWorkout by remember { mutableStateOf(false) }
    var showCreateExercise by remember { mutableStateOf(false) }
    val options = listOf(R.string.workouts, R.string.exercises, R.string.history)
    var selected by rememberSaveable { mutableIntStateOf(R.string.workouts) }
    val floatingActionButton = getFloatingActionButton(
        selected = selected,
        showCreateWorkout = { showCreateWorkout = true },
        showCreateExercise = { showCreateExercise = true }
    )
    val content = getContent(
        selected = selected,
        workoutNavigationFunction = workoutNavigationFunction,
        exerciseNavigationFunction = exerciseNavigationFunction,
        showCreateWorkout = showCreateWorkout,
        dismissCreateWorkout = { showCreateWorkout = false },
        showCreateExercise = showCreateExercise,
        dismissCreateExercise = { showCreateExercise = false }
    )
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = stringResource(id = R.string.home),
                navController = navController,
                homeScreen = true
            )
        },
        floatingActionButton = {
            floatingActionButton()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                for (option in options) {
                    Button(
                        onClick = { selected = option },
                        enabled = selected != option
                    ) {
                        Text(text = stringResource(id = option))
                    }
                }
            }
            content()
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

private fun getFloatingActionButton(
    selected: Int,
    showCreateWorkout: () -> Unit,
    showCreateExercise: () -> Unit
): @Composable () -> Unit {
    return when (selected) {
        R.string.workouts -> {
            {
                FloatingActionButton(
                    onClick = showCreateWorkout,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.Black,
                        contentDescription = stringResource(id = R.string.add_workout)
                    )
                }
            }
        }

        R.string.exercises -> {
            {
                FloatingActionButton(
                    onClick = showCreateExercise,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.Black,
                        contentDescription = stringResource(id = R.string.add_exercise)
                    )
                }
            }
        }

        R.string.history -> {
            { }
        }

        else -> {
            { }
        }
    }
}

fun getContent(
    selected: Int,
    exerciseNavigationFunction: (Int) -> Unit,
    workoutNavigationFunction: (Int) -> Unit,
    showCreateWorkout: Boolean,
    dismissCreateWorkout: () -> Unit,
    showCreateExercise: Boolean,
    dismissCreateExercise: () -> Unit
): @Composable () -> Unit {
    return when (selected) {
        R.string.workouts -> {
            {
                WorkoutsScreen(
                    workoutNavigationFunction = workoutNavigationFunction,
                    showCreateWorkout = showCreateWorkout,
                    dismissCreateWorkout = dismissCreateWorkout
                )
            }
        }

        R.string.exercises -> {
            {
                ExercisesScreen(
                    exerciseNavigationFunction = exerciseNavigationFunction,
                    showCreateExercise = showCreateExercise,
                    dismissCreateExercise = dismissCreateExercise
                )
            }
        }

        R.string.history -> {
            {
                OverallHistoryScreen(
                    exerciseNavigationFunction = exerciseNavigationFunction,
                    workoutNavigationFunction = workoutNavigationFunction
                )
            }
        }

        else -> {
            { }
        }
    }
}
