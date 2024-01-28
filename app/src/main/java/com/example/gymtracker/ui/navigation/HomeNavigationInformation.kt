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
    return NavigationRoutes.values()
        .filter { it.homeRoute }
        .associate { navigationArgument ->
            HomeNavigationInformation(navigationArgument.homeNavigationTitle) {
                navController.navigate(navigationArgument.baseRoute)
            } to (navigationArgument.baseRoute != homeRoute.route)
        }
}
