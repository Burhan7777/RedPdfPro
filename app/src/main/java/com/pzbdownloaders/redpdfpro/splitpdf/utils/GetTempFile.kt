package com.pzbdownloaders.redpdfpro.splitpdf.utils

import android.net.Uri
import com.pzbdownloaders.redpdfpro.MainActivity

fun getFilePathFromContentUri(uri: Uri, activity: MainActivity): String? {
    return activity.contentResolver.openInputStream(uri)?.use { inputStream ->
        // Create a temporary file and copy the content of the input stream
        val tempFile = createTempFile("temp_pdf", ".pdf", activity.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        // Return the absolute path of the temporary file
        tempFile.absolutePath
    }
}