package com.pzbdownloaders.redpdfpro.scannerfeature.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.runtime.MutableState
import com.chaquo.python.Python
import com.googlecode.tesseract.android.TessBaseAPI
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import java.io.File
import java.io.FileOutputStream


fun convertPdfToImage(
    context: Context,
    path: String,
    nameOfWordFIle: MutableState<String>,
    showProgressDialogBoxOfWordFile: MutableState<Boolean>
) {
    var listOfBitmaps: ArrayList<Bitmap> = ArrayList()
    val listOfFIlePaths: ArrayList<String> = ArrayList()
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
    println(listOfBitmaps.size)
    var tempFolder = File("storage/emulated/0/Download/Pro Scanner/temp_images")
    if (!tempFolder.exists())
        tempFolder.mkdirs()


    for (i in listOfBitmaps.indices) {
        val tempFIle =
            File("storage/emulated/0/Download/Pro Scanner/temp_images/image{$i}.jpg")
        if (!tempFIle.exists())
            tempFIle.createNewFile()

        val fos =
            FileOutputStream("storage/emulated/0/Download/Pro Scanner/temp_images/image{$i}.jpg")
        listOfBitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, fos)
        listOfFIlePaths.add("storage/emulated/0/Download/Pro Scanner/temp_images/image{$i}.jpg")
    }
    getTextFromPdf(context, listOfFIlePaths, nameOfWordFIle, showProgressDialogBoxOfWordFile)
}

fun getTextFromPdf(
    context: Context,
    listOfFilePaths: List<String>,
    nameOfWordFIle: MutableState<String>,
    showProgressDialogBoxOfWordFile: MutableState<Boolean>
) {
    var assets = context.assets.open("eng.traineddata")
    val folder = File("${context.filesDir}/tessdata/")
    if (!folder.exists()) {
        folder.mkdirs()
    }
    val file = File("${context.filesDir}/tessdata/eng.traineddata")
    if (!file.exists()) {
        file.createNewFile()
    }
    var fos = FileOutputStream("${context.filesDir}/tessdata/eng.traineddata")
    assets.use {
        it.copyTo(fos)
    }
    var finalText = ""
    var tesseract = TessBaseAPI()
    var path = File("${context.filesDir}").absolutePath
    if (!tesseract.init(path, "eng")) {
        tesseract.recycle()
    }
    for (i in listOfFilePaths.indices) {
        /// java.lang.RuntimeException: Failed to read image file
        // catch the error // this means wrong path
        tesseract.setImage(File(listOfFilePaths[i]))
        finalText += tesseract.utF8Text
    }
    addTextToTheWordFile(finalText, nameOfWordFIle.value, context, showProgressDialogBoxOfWordFile)
}

fun addTextToTheWordFile(
    text: String,
    name: String,
    context: Context,
    showProgressDialogBoxOfWordFile: MutableState<Boolean>
) {
    var docxFolder = File("storage/emulated/0/Download/Pro Scanner/docx/")
    if (!docxFolder.exists()) {
        docxFolder.mkdirs()
    }
    val python = Python.getInstance()
    val module = python.getModule("pdftodocx")
    module.callAttr("create_docx_from_text", text, name)
    showProgressDialogBoxOfWordFile.value = false
}

