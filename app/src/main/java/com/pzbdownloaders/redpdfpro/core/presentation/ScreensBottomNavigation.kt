package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.ui.graphics.vector.ImageVector
import com.pzbdownloaders.redpdfpro.R

sealed class ScreensBottomNavigation(
    val route: String,
    val title: String,
    val imageVector: ImageVector = Icons.Default.Person,
    val painterId: Int = 0
) {
    object ScannerScreen :
        ScreensBottomNavigation("scanner_tools_screen", "Scanner", Icons.Default.Scanner)

    object HomePageTools :
        ScreensBottomNavigation("homepage_tools_screen", "Tools", Icons.Default.Build)

    object Documents :
        ScreensBottomNavigation(
            Screens.Documents.route,
            "Pdfs",
            painterId = R.drawable.pdf_bottom_navigation
        )

}
