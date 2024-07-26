package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable

interface HomeScreenView {
    fun getFloatingActionButton(
        homeScreenVariables: HomeScreenVariables,
        homeScreenVariablesOnChange: (HomeScreenVariables) -> Unit
    ): @Composable () -> Unit
}