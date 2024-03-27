package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pzbdownloaders.docstopdffeature.DocsToPdf
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.compresspdffeature.screens.CompressPDF
import com.pzbdownloaders.redpdfpro.extractimagefeature.ExtractImage
import com.pzbdownloaders.redpdfpro.extracttextfeature.ExtractText
import com.pzbdownloaders.redpdfpro.imagetopdffeature.ImageToPdf
import com.pzbdownloaders.redpdfpro.lockpdffeature.LockPdf
import com.pzbdownloaders.redpdfpro.mergepdffeature.screens.MergePdf
import com.pzbdownloaders.redpdfpro.rotatepdffeature.screens.RotatePDf
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.SplitPdf
import com.pzbdownloaders.redpdfpro.unlockpdffeature.UnlockPdf


fun NavGraphBuilder.toolsGraph(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    navigation(
        route = TOOLS_GRAPH,
        startDestination = ScreensBottomNavigation.HomePageTools.route
    ) {
        composable(route = ScreensBottomNavigation.HomePageTools.route) {
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
        composable(Screens.LockPdf.route) {
            LockPdf(activity)
        }
        composable(Screens.UnlockPdf.route) {
            UnlockPdf(activity)
        }
        composable(Screens.ExtractImageFromPdf.route) {
            ExtractImage(activity = activity)
        }
        composable(Screens.ImageToPdf.route) {
            ImageToPdf(activity)
        }
        composable(Screens.DocsToPdf.route) {
            DocsToPdf(activity)
        }
    }
}