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
    page.render(bitmap, clipRect,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()
    return bitmap
//    val page = renderer.openPage(pageIndex)
//
//    // Scale down the image if necessary (adjust scale factor as needed)
//    val scaleFactor = 2 // or 3, depending on how much you want to reduce resolution
//    val width = page.width / scaleFactor
//    val height = page.height / scaleFactor
//
//    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmap)
//
//    val clipRect = Rect(0, 0, width, height)
//    canvas.drawColor(Color.WHITE)
//    canvas.clipRect(clipRect)
//
//    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//    page.close()
//
//    return bitmap
}