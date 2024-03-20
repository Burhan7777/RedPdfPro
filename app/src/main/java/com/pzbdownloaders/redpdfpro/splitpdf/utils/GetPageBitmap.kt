package com.pzbdownloaders.redpdfpro.splitpdf.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.isSystemInDarkTheme
import java.io.File

fun loadPage(
    context: Context,
    file: File,
    pageIndex: Int,
    renderer: PdfRenderer,
    darkTheme: Boolean
): Bitmap {
    val page: PdfRenderer.Page = renderer.openPage(pageIndex)
    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
    if (darkTheme) {
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
    }
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()
    return bitmap
}