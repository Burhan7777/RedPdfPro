package com.pzbdownloaders.redpdfpro.documentfeature.util

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.scannerfeature.util.scanFile
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun savePdfAsImageInTempFolder(path: String): ArrayList<String> {
    var listOfBitmaps: ArrayList<Bitmap> = ArrayList()
    var pathOfBitmaps: ArrayList<String> = ArrayList()
    var parcelFileDescriptor =
        ParcelFileDescriptor.open(
            File(path),
            ParcelFileDescriptor.MODE_READ_ONLY
        )
    var pdfRenderer = PdfRenderer(parcelFileDescriptor)
    val totalPages = pdfRenderer.pageCount
    for (i in 0 until totalPages.toString().toInt()) {
        var bitmap = loadPage(
            i,
            pdfRenderer
        )
        listOfBitmaps.add(bitmap)
    }

    for (i in listOfBitmaps.indices) {
        val externalDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var pathOfBitmap = "$externalDir/Pro Scanner/temp_images/image-${UUID.randomUUID()}.jpg"
        pathOfBitmaps.add(pathOfBitmap)
        val folder = File("$externalDir/Pro Scanner/temp_images")
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
    return pathOfBitmaps
}