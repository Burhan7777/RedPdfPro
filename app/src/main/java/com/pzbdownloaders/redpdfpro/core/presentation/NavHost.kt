package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
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
    }

}