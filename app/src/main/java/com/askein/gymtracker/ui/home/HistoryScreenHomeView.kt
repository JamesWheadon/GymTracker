package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable
import com.askein.gymtracker.ui.history.OverallHistoryScreen

class HistoryScreenHomeView : HomeScreenView {
    @Composable
    override fun FloatingActionButton(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ) {
    }

    @Composable
    override fun ScreenContent(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ) {
        OverallHistoryScreen(
            exerciseNavigationFunction = homeData.exerciseNavigationFunction,
            workoutNavigationFunction = homeData.workoutNavigationFunction
        )
    }
}
