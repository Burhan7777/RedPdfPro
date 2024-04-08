package com.pzbdownloaders.redpdfpro.scannerfeature.util

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import java.io.File

data class ScannerModel(
    val name: String?,
    val bitmap: Bitmap?,
    val path: String?
)
