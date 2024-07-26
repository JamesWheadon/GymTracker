package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable

class HistoryScreenHomeView : HomeScreenView {
    override fun getFloatingActionButton(
        homeScreenVariables: HomeScreenVariables,
        homeScreenVariablesOnChange: (HomeScreenVariables) -> Unit
    ): @Composable () -> Unit {
        return { }
    }
}
