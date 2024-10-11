package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.displaypdffeature.PdfViewer
import com.pzbdownloaders.redpdfpro.scannerfeature.screens.ScannerScreen

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
            ScannerScreen(activity = activity, viewModel, navHostController)
        }
        composable(Screens.PdfViewer.route, arguments = listOf(navArgument("uri") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("file") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            PdfViewer(uri = it.arguments?.getString("uri")!!, file = it.arguments?.getString("file")!!)
        }
    }
}