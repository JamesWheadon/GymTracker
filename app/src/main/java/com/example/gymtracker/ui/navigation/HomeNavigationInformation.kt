package com.example.gymtracker.ui.navigation

import androidx.navigation.NavHostController

data class HomeNavigationInformation(
    val title: String,
    val navigationFunction: () -> Unit
)

fun getHomeNavigationOptionsForRoute(
    homeRoute: NavigationRoute,
    navController: NavHostController
): Map<HomeNavigationInformation, Boolean> {
    return NavigationArguments.values()
        .filter { it.homeRoute }
        .associate { navigationArgument ->
            HomeNavigationInformation(navigationArgument.homeNavigationTitle) {
                navController.navigate(navigationArgument.route)
            } to (navigationArgument.route != homeRoute.route)
        }
}
