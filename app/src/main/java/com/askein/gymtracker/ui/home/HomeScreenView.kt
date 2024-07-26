package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable

interface HomeScreenView {
    fun getFloatingActionButton(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit

    fun getScreenContent(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    ): @Composable () -> Unit
}