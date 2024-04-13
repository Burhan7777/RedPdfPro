package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MyNavHost(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    androidx.navigation.compose.NavHost(
        navController = navHostController,
        startDestination = SCANNER_GRAPH
    ) {
        toolsGraph(navHostController, activity = activity, viewModel)
        scannerGraph(navHostController, activity, viewModel)
        documentGraph(activity,viewModel,navHostController)
    }

}