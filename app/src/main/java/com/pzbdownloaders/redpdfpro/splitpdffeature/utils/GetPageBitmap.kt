package com.pzbdownloaders.redpdfpro.splitpdffeature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import java.io.File

fun loadPage(
    pageIndex: Int,
    renderer: PdfRenderer
): Bitmap {
    val page: PdfRenderer.Page = renderer.openPage(pageIndex)
    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()
    return bitmap
}