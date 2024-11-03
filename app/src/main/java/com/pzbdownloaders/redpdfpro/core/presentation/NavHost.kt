package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.graphs.aiGraph
import com.pzbdownloaders.redpdfpro.core.presentation.graphs.documentGraph
import com.pzbdownloaders.redpdfpro.core.presentation.graphs.scannerGraph
import com.pzbdownloaders.redpdfpro.core.presentation.graphs.toolsGraph

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
        aiGraph(activity,viewModel,navHostController)
    }

}