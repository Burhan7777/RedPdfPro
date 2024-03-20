package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.SplitPdf

@Composable
fun MyNavHost(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    androidx.navigation.compose.NavHost(
        navController = navHostController,
        startDestination = Screens.homePage.route
    ) {
        composable(route = Screens.homePage.route) {
            HomePage(navHostController, viewModel )
        }
        composable(route = Screens.splitPdf.route) {
            SplitPdf(navHostController, activity, viewModel)
        }
    }

}