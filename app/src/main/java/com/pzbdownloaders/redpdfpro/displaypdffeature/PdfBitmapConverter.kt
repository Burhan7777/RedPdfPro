package com.pzbdownloaders.redpdfpro.displaypdffeature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PdfBitmapConverter(
    private var context: Context,
    var uri: String,
    var file: String
) {
    var renderer: PdfRenderer? = null
    var parcelFileDescriptor =
        ParcelFileDescriptor.open(
            File(file),
            ParcelFileDescriptor.MODE_READ_ONLY
        )
    var list: ArrayList<Bitmap> = ArrayList()
    var scope = CoroutineScope(Dispatchers.IO)
    suspend fun pdfToBitmaps(contentUri: Uri): List<Bitmap> {
//        return withContext(Dispatchers.IO) {
//            renderer?.close()
//            context.contentResolver.openFileDescriptor(
//                contentUri, "r"
//            )?.use { descriptor ->
//                with(PdfRenderer(descriptor)) {
//                    renderer = this
//                    return@withContext (0..pageCount ).map { index ->
//                        async {
//                            openPage(index).use { page ->
//                                val bitmap = Bitmap.createBitmap(
//                                    page.width,
//                                    page.height,
//                                    Bitmap.Config.ARGB_8888
//                                )
//                                Canvas(bitmap).apply {
//                                    drawColor(android.graphics.Color.argb(255, 255, 255, 255))
//                                    drawBitmap(bitmap, 0f, 0f, null)
//                                }
//                                page.render(
//                                    bitmap,
//                                    null,
//                                    null,
//                                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
//                                )
//
//                                bitmap
//
//                            }
//                        }
//                    }.awaitAll()
//                }
//            }
//            return@withContext emptyList()
//        }

        var pdfRenderer = PdfRenderer(parcelFileDescriptor)
        for (i in 0 until pdfRenderer.pageCount) {
            scope.async {
                var page = pdfRenderer.openPage(i)
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                var canvas = Canvas(bitmap)
                canvas.drawColor(android.graphics.Color.argb(255, 255, 255, 255))
               var clipRect = Rect(0, 0, page.width, page.height);
                canvas.clipRect(clipRect)
                page.render(bitmap, clipRect, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                list.add(bitmap)
                withContext(Dispatchers.Main) {
                    page.close()
                }
            }.await()

        }
        return list
    }
}

