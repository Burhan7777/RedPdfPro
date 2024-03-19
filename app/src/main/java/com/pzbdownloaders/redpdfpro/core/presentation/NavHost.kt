package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun MyNavHost(navHostController: NavHostController) {
    androidx.navigation.compose.NavHost(
        navController = navHostController,
        startDestination = Screens.homePage.route
    ) {
        composable(route = Screens.homePage.route) {
            HomePage(navHostController)
        }
    }

}