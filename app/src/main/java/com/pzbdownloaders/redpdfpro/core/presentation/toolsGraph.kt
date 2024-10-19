package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.docstopdffeature.DocsToPdf
import com.pzbdownloaders.redpdfpro.compresspdffeature.screens.CompressPDF
import com.pzbdownloaders.redpdfpro.extractimagefeature.ExtractImage
import com.pzbdownloaders.redpdfpro.extracttextfeature.ExtractText
import com.pzbdownloaders.redpdfpro.imagetopdffeature.ImageToPdf
import com.pzbdownloaders.redpdfpro.lockpdffeature.LockPdf
import com.pzbdownloaders.redpdfpro.mergepdffeature.screens.MergePdf
import com.pzbdownloaders.redpdfpro.rotatepdffeature.screens.RotatePDf
import com.pzbdownloaders.redpdfpro.rotatepdffeature.screens.ViewPdfRotateScreen
import com.pzbdownloaders.redpdfpro.scantodocxfeature.ScanToDocx
import com.pzbdownloaders.redpdfpro.scantotxtfeature.ScanToTxt
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.SplitPdf
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.ViewSplitPdfScreen
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
        composable(route = Screens.SplitPdf.route, arguments = listOf(navArgument("filePath") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            SplitPdf(navHostController, activity, viewModel, it.arguments?.getString("filePath"))
        }
        composable(Screens.MergePdf.route, arguments = listOf(navArgument("fileName") {
            type = NavType.StringType
            defaultValue = ""
        },
            navArgument("filePath") {
                type = NavType.StringType
                defaultValue = ""
            }
        )) {
            MergePdf(
                activity = activity,
                viewModel,
                it.arguments?.getString("fileName"),
                it.arguments?.getString("filePath"),
                navHostController
            )
        }
        composable(Screens.CompressPDf.route) {
            CompressPDF(mainActivity = activity)
        }
        composable(Screens.ExtractText.route) {
            ExtractText(activity,viewModel)
        }
        composable(Screens.RotatePdf.route) {
            RotatePDf(navHostController, viewModel, activity)
        }
        composable(Screens.LockPdf.route) {
            LockPdf(activity, viewModel, navHostController)
        }
        composable(Screens.UnlockPdf.route) {
            UnlockPdf(activity, viewModel, navHostController)
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
        composable(Screens.ScanToDocx.route) {
            ScanToDocx(activity, viewModel)
        }
        composable(Screens.ScanToTxt.route) {
            ScanToTxt(activity, viewModel)
        }
        composable(Screens.ViewSplitPdfScreen.route, arguments = listOf(navArgument("path") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("uri") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            ViewSplitPdfScreen(
                activity,
                viewModel,
                navHostController,
                it.arguments?.getString("path") ?: "",
                it.arguments?.getString("uri") ?: ""
            )
        }
        composable(
            Screens.FinalScreenOfPdfOperations.route,
            arguments = listOf(navArgument("path") {
                type = NavType.StringType
                defaultValue = ""
            }, navArgument("uri") {
                type = NavType.StringType
                defaultValue = ""
            }, navArgument("pathOfUnlockedFile") {
                type = NavType.StringType
                defaultValue = " "
            })
        ) {
            FinalScreenOfPdfOperations(
                activity,
                navHostController,
                path = it.arguments?.getString("path") ?: "",
                uri = it.arguments?.getString("uri") ?: "",
                pathOfUnlockedFIle = it.arguments?.getString("pathOfUnlockedFile") ?: ""
            )
        }
        composable(Screens.ViewPdfRotateScreen.route, arguments = listOf(navArgument("path") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            ViewPdfRotateScreen(
                activity,
                viewModel,
                navHostController,
                it.arguments?.getString("path") ?: "",
            )
        }
    }
}