package com.pzbdownloaders.redpdfpro.extractimagefeature.util

import android.content.Context
import com.shockwave.pdfium.PdfiumCore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.Component.scanFile
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun extractImagesFromPDFWithPdfium(
    pdfFile: File,
    context: Context,
    scope: CoroutineScope
) {
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


fun extractImagesFromPDFWithPDFBoxAndroid(
    pdfFile: File,
    context: Context,
    scope: CoroutineScope,
    showExtractingLoadingBox: MutableState<Boolean>,
    navHostController: NavHostController,
    viewModel: MyViewModel
) {
    var externalDir =
        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}"
    var imageExtractedCount = 0
    // Load the PDF document
    scope.launch(Dispatchers.Default) {
        showExtractingLoadingBox.value = true
        PDDocument.load(pdfFile).use { document ->
            var imageIndex = 0
            // Iterate over each page in the document
            for (page in document.pages) {
                val resources = page.resources
                for (xObjectName in resources.xObjectNames) {
                    var randomUUID = UUID.randomUUID()
                    val xObject = resources.getXObject(xObjectName)
                    if (xObject is PDImageXObject) {
                        // Extract image data as a Bitmap
                        val imageStream = xObject.createInputStream()
                        val bitmap = BitmapFactory.decodeStream(imageStream)
                        imageStream.close()


                        // Save the bitmap to the output directory
                        if (bitmap != null) {
                            val outputFile =
                                File(
                                    externalDir,
                                    "Pro Scanner/images/image$imageIndex-${randomUUID}.png"
                                )
                            FileOutputStream(outputFile).use { out ->
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                            }
                            scanFile(outputFile.absolutePath, context)
                            viewModel.listOfImagesFromExtractImages.add(outputFile.absolutePath)
                            imageExtractedCount++
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Failed to decode image at page : ${document.pages.indexOf(page)}, skipping that image",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                        imageIndex++
                    }
                }
            }
            withContext(Dispatchers.Main) {
                showExtractingLoadingBox.value = false
                if (imageExtractedCount > 0) {
                    navHostController.navigate(
                        Screens.FinalScreenOFImageExtraction.route
                    )
                } else {
                    Toast.makeText(context, "No image was extracted from pdf", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}