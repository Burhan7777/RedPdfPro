package com.pzbdownloaders.redpdfpro.core.presentation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.core.presentation.DOCUMENTS_GRAPH
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.documentfeature.screens.DocumentFeature
import com.pzbdownloaders.redpdfpro.pdfreaderfeature.screens.PdfReader

fun NavGraphBuilder.documentGraph(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    navigation(startDestination = Screens.Documents.route, route = DOCUMENTS_GRAPH) {
        composable(Screens.Documents.route) {
            DocumentFeature(activity, viewModel, navHostController)
        }
        composable(Screens.PdfReader.route, arguments = listOf(navArgument("uri") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("file") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            PdfReader(
                activity,
                viewModel,
                navHostController,
                it.arguments?.getString("uri")!!,
                it.arguments?.getString("file") ?: ""
            )
        }
    }
}