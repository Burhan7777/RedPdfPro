package com.pzbdownloaders.scannerfeature.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun downloadPdfAsJpeg(path: String): String {
    var listOfBitmaps: ArrayList<Bitmap> = ArrayList()
    val python = Python.getInstance()
    val module = python.getModule("splitPDF")
    val totalPages = module.callAttr("total_pages", path)
    val file = File(path)
    for (i in 0 until totalPages.toString().toInt()) {
        var parcelFileDescriptor =
            ParcelFileDescriptor.open(
                file,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        var pdfRenderer = PdfRenderer(parcelFileDescriptor)
        var bitmap = loadPage(
            i,
            pdfRenderer
        )
        listOfBitmaps.add(bitmap)
    }

    for (i in listOfBitmaps.indices) {
        val externalDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var pathOfBitmap = "$externalDir/Pro Scanner/images/image-${UUID.randomUUID()}.jpg"
        val folder = File("$externalDir/Pro Scanner/images")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val file = File(pathOfBitmap)
        if (!file.exists()) {
            file.createNewFile()
        }
        var fileOutputStream = FileOutputStream(file)
        listOfBitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
    }

    return "Done"
}