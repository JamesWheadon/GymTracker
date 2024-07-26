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

class ExercisesScreenHomeView : HomeScreenView {
    override fun getFloatingActionButton(
        homeScreenVariables: HomeScreenVariables,
        homeScreenVariablesOnChange: (HomeScreenVariables) -> Unit
    ): @Composable () -> Unit {
        return {
            FloatingActionButton(
                onClick = { homeScreenVariablesOnChange(homeScreenVariables.copy(showCreateExercise = true)) },
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
}
