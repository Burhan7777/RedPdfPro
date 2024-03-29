package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ScreensBottomNavigation(
    val route: String,
    val title: String,
    val imageVector: ImageVector
) {
    object ScannerScreen :
        ScreensBottomNavigation("scanner_tools_screen", "Scanner", Icons.Default.Scanner)

    object HomePageTools :
        ScreensBottomNavigation("homepage_tools_screen", "Tools", Icons.Default.Build)

}
