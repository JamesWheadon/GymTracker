package com.askein.gymtracker.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.workout.WorkoutsScreen

class WorkoutsScreenHomeView : HomeScreenView {
    override fun getFloatingActionButton(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit {
        return {
            FloatingActionButton(
                onClick = { homeDataOnChange(homeData.copy(showCreateWorkout = true)) },
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

    override fun getScreenContent(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit {
        return {
            WorkoutsScreen(
                workoutNavigationFunction = homeData.workoutNavigationFunction,
                showCreateWorkout = homeData.showCreateWorkout,
                dismissCreateWorkout = { homeDataOnChange(homeData.copy(showCreateWorkout = false)) }
            )
        }
    }
}
