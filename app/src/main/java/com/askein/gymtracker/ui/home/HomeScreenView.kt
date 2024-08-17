package com.askein.gymtracker.ui.home

import androidx.compose.runtime.Composable

interface HomeScreenView {
    @Composable
    fun FloatingActionButton(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    )

    @Composable
    fun ScreenContent(
        homeData: HomeData,
        homeDataOnChange: (HomeData) -> Unit
    )
}