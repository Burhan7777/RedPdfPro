package com.pzbdownloaders.redpdfpro.splitpdffeature.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import java.io.File

fun loadPage(
    pageIndex: Int,
    renderer: PdfRenderer
): Bitmap {
    val page: PdfRenderer.Page = renderer.openPage(pageIndex)
    val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    canvas.drawColor(Color.argb(255, 255, 255, 255))
    var clipRect = Rect(0, 0, page.width, page.height);
    canvas.clipRect(clipRect)
    page.render(bitmap, clipRect,null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT)
    page.close()
    return bitmap
}