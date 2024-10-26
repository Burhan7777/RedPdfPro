package com.pzbdownloaders.redpdfpro.extractimagefeature.util

import android.content.Context
import com.shockwave.pdfium.PdfiumCore
import android.graphics.Bitmap
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import com.pzbdownloaders.redpdfpro.core.presentation.Component.scanFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun extractImagesFromPDFWithPdfium(pdfFile: File, context: Context, scope: CoroutineScope) {
    scope.launch(Dispatchers.Default) {
        val pdfiumCore = PdfiumCore(context)
        val pdfDocument = pdfiumCore.newDocument(
            ParcelFileDescriptor.open(
                pdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        )

        val pageCount = pdfiumCore.getPageCount(pdfDocument)
        for (pageIndex in 0 until pageCount) {
            var randomUUID = UUID.randomUUID()
            pdfiumCore.openPage(pdfDocument, pageIndex)

            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageIndex)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageIndex)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageIndex, 0, 0, width, height)
            var externalDir =
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}"
            // Save the bitmap as a PNG file
            val file =
                File(externalDir, "Pro Scanner/images/image$pageIndex-${randomUUID}.png")

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            scanFile("$externalDir/Pro Scanner/images/image$pageIndex-${randomUUID}.png", context)
        }
        pdfiumCore.closeDocument(pdfDocument)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
}
