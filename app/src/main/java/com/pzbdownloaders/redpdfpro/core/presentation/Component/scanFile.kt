package com.pzbdownloaders.redpdfpro.core.presentation.Component

import android.content.Context
import android.media.MediaScannerConnection

fun scanFile(filePath: String, context: Context) {
    MediaScannerConnection.scanFile(
        context,
        arrayOf(filePath),
        null
    ) { path, uri ->
        // Callback invoked after scanning is complete
        // You can perform any additional actions here if needed
    }
}