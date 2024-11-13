package com.pzbdownloaders.redpdfpro.core.presentation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.docstopdffeature.DocsToPdf
import com.pzbdownloaders.redpdfpro.compresspdffeature.screens.CompressPDF
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.PdfToPptfeature.presentation.screen.PdfToPptScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftocsvfeature.presentation.screens.PdfToCsvScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftodocfeature.presentation.screen.PdfToDocScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftodocxfeature.presentation.screens.PdfToDocxScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftoepubfeature.presentation.screens.PdfToEpubScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftohtml5feature.presentation.screen.PdfToHtml5Screen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftojpgfeature.presentation.screens.PdfToJpgScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftopptxfeature.presentation.screens.PdfToPptx
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftoxlsfeature.presentation.screen.PdfToXlsScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertPdfToAnyFormat.pdftoxlsxfeature.presentation.screen.PdfToXlsxScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.csvtopdffeature.presentation.screens.CsvToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.doctopdffeature.presentation.screens.DocToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.epubtopdffeature.presentation.screen.EpubToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.ppttopdffeature.presentation.screens.PptToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.pptxtopdffeature.presentation.screens.PptxToPdfFeature
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.xlstopdffeature.presentation.screens.XlsToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.xlsxtopdffeature.presentation.screens.XlsxToPdfScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.docx.converttodocx.convertdoctodocx.presentation.screen.DocToDocxScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.otherconversions.csvtoxlsfeature.presentation.screen.CsvToXlsScreen
import com.pzbdownloaders.redpdfpro.conversionsfeature.otherconversions.csvtoxlsxfeature.presentation.screen.CsvToXlsxScreen
import com.pzbdownloaders.redpdfpro.core.presentation.HomePage
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.core.presentation.ScreensBottomNavigation
import com.pzbdownloaders.redpdfpro.core.presentation.TOOLS_GRAPH
import com.pzbdownloaders.redpdfpro.core.presentation.finalscreens.FinalScreen
import com.pzbdownloaders.redpdfpro.core.presentation.finalscreens.FinalScreenForImageExtraction
import com.pzbdownloaders.redpdfpro.core.presentation.finalscreens.FinalScreenOfPdfOperations
import com.pzbdownloaders.redpdfpro.core.presentation.finalscreens.FinalScreenOfTextExtraction
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
        composable(
            Screens.MergePdf.route, arguments = listOf(navArgument("fileName") {
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
            ExtractText(activity, viewModel, navHostController)
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
            ExtractImage(activity = activity, viewModel, navHostController)
        }
        composable(Screens.ImageToPdf.route) {
            ImageToPdf(activity, navHostController)
        }
        composable(Screens.DocsToPdf.route) {
            DocsToPdf(activity, viewModel, navHostController)
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
        composable(
            Screens.FinalScreenOFImageExtraction.route, listOf(navArgument("pathOfTempFile") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            FinalScreenForImageExtraction(
                activity,
                navHostController,
                viewModel,
                it.arguments?.getString("pathOfTempFile") ?: ""
            )
        }
        composable(Screens.FinalScreenOfTextExtraction.route, listOf(navArgument("pathOfFile") {
            type = NavType.StringType
            defaultValue = ""
        })) {
            FinalScreenOfTextExtraction(navHostController, it.arguments?.getString("pathOfFile")!!)
        }
        composable(Screens.PptxToPdf.route) {
            PptxToPdfFeature(activity, navHostController, viewModel)
        }
        composable(Screens.PdfToDocxScreen.route) {
            PdfToDocxScreen(activity, viewModel, navHostController)
        }
        composable(Screens.XlsToPdf.route) {
            XlsToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.XlsxToPdf.route) {
            XlsxToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.DocToPdf.route) {
            DocToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PptToPdf.route) {
            PptToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.CsvToPdf.route) {
            CsvToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.EpubToPdf.route) {
            EpubToPdfScreen(activity, viewModel, navHostController)
        }
        composable(Screens.FinalScreen.route, listOf(navArgument("pathOfFile") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("pathOfDir") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("mimeType") {
            type = NavType.StringType
            defaultValue = ""
        }, navArgument("stringResource") {
            type = NavType.IntType
            defaultValue = 0
        })) {
            FinalScreen(
                navHostController,
                it.arguments?.getString("pathOfFile")!!,
                it.arguments?.getString("pathOfDir")!!,
                it.arguments?.getString("mimeType")!!,
                it.arguments?.getInt("stringResource")!!
            )
        }
        composable(Screens.PdfToJpgScreen.route) {
            PdfToJpgScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToDocScreen.route) {
            PdfToDocScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToPptxScreen.route) {
            PdfToPptx(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToPptScreen.route) {
            PdfToPptScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToEpubScreen.route) {
            PdfToEpubScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToCsvScreen.route) {
            PdfToCsvScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToXls.route) {
            PdfToXlsScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToXlsx.route) {
            PdfToXlsxScreen(activity, viewModel, navHostController)
        }
        composable(Screens.PdfToHtml5.route) {
            PdfToHtml5Screen(activity, viewModel, navHostController)
        }
        composable(Screens.DocToDOcx.route) {
            DocToDocxScreen(activity, viewModel, navHostController)
        }
        composable(Screens.CsvToXls.route) {
            CsvToXlsScreen(activity, viewModel, navHostController)
        }
        composable(Screens.CsvToXlsx.route) {
            CsvToXlsxScreen(activity, viewModel, navHostController)
        }
    }
}