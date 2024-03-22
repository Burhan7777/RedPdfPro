package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.compresspdffeature.screens.CompressPDF
import com.pzbdownloaders.redpdfpro.extracttextfeature.ExtractText
import com.pzbdownloaders.redpdfpro.mergepdffeature.screens.MergePdf
import com.pzbdownloaders.redpdfpro.rotatepdffeature.screens.RotatePDf
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.SplitPdf

@Composable
fun MyNavHost(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    androidx.navigation.compose.NavHost(
        navController = navHostController,
        startDestination = Screens.HomePage.route
    ) {
        composable(route = Screens.HomePage.route) {
            HomePage(navHostController, viewModel)
        }
        composable(route = Screens.SplitPdf.route) {
            SplitPdf(navHostController, activity, viewModel)
        }
        composable(Screens.MergePdf.route) {
            MergePdf(activity = activity, viewModel)
        }
        composable(Screens.CompressPDf.route) {
            CompressPDF(mainActivity = activity)
        }
        composable(Screens.ExtractText.route) {
            ExtractText(activity)
        }
        composable(Screens.RotatePdf.route) {
            RotatePDf(navHostController, viewModel, activity)
        }
    }

}