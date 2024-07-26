package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable
import com.askein.gymtracker.ui.history.OverallHistoryScreen

class HistoryScreenHomeView : HomeScreenView {
    override fun getFloatingActionButton(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit {
        return { }
    }

    override fun getScreenContent(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit {
        return {
            OverallHistoryScreen(
                exerciseNavigationFunction = homeData.exerciseNavigationFunction,
                workoutNavigationFunction = homeData.workoutNavigationFunction
            )
        }
    }
}
