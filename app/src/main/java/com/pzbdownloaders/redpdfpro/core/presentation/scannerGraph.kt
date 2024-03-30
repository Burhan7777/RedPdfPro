package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.scannerfeature.screens.ScannerScreen

fun NavGraphBuilder.scannerGraph(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    navigation(
        route = SCANNER_GRAPH,
        startDestination = ScreensBottomNavigation.ScannerScreen.route
    ) {
        composable(ScreensBottomNavigation.ScannerScreen.route) {
            ScannerScreen(activity = activity,viewModel,navHostController)
        }
    }
}