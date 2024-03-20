package com.askein.gymtracker.ui.navigation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.askein.gymtracker.R
import com.askein.gymtracker.ui.user.UserPreferencesRoute
import com.askein.gymtracker.ui.workout.WorkoutSelectionScreenRoute
import com.askein.gymtracker.ui.workout.history.create.live.LiveRecordWorkoutRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    text: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeScreen: Boolean = false,
    settingsScreen: Boolean = false,
    editFunction: (() -> Unit)? = null,
    deleteFunction: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        },
        navigationIcon = {
            val homeRoutes = NavigationRoutes.values()
                .filter { it.homeRoute }
                .map { it.baseRoute }
            Row {
                if (navController.currentDestination != null && !homeRoutes.contains(navController.currentDestination!!.route)) {
                    IconButton(onClick = { navController.navigate(navController.graph.startDestinationId) }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = stringResource(id = R.string.home_button)
                        )
                    }
                }
                if (navController.previousBackStackEntry != null) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button)
                        )
                    }
                }
            }
        },
        actions = {
            if (editFunction != null) {
                IconButton(onClick = editFunction) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(id = R.string.edit_feature)
                    )
                }
            }
            if (deleteFunction != null) {
                IconButton(onClick = deleteFunction) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        tint = Color.Red,
                        contentDescription = stringResource(id = R.string.delete_feature)
                    )
                }
            }
            if (!settingsScreen) {
                IconButton(onClick = { navController.navigate(route = UserPreferencesRoute.route) }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = stringResource(id = R.string.user_settings)
                    )
                }
            }
            if (homeScreen) {
                IconButton(onClick = {
                    if (!navController.popBackStack(
                            route = LiveRecordWorkoutRoute.route,
                            inclusive = false
                        )
                    ) {
                        navController.navigate(WorkoutSelectionScreenRoute.route)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = stringResource(id = R.string.live_record_workout)
                    )
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        modifier = modifier
    )
}
