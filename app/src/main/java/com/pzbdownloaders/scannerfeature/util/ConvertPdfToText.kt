package com.pzbdownloaders.scannerfeature.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.runtime.MutableState
import com.chaquo.python.Python
import com.googlecode.tesseract.android.TessBaseAPI
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


fun getImagesForTextFiles(
    context: Context,
    path: String,
    nameOfWordFIle: MutableState<String>,
    showProgressDialogBoxOfTextFile: MutableState<Boolean>
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
    saveFile(
        context,
        listOfFIlePaths,
        nameOfWordFIle,
        showProgressDialogBoxOfTextFile
    )
}


fun saveFile(
    context: Context,
    listOfFilePaths: List<String>,
    nameOfTextFile: MutableState<String>,
    showProgressDialogBoxOfTextFile: MutableState<Boolean>
) {
    /*    var assets = context.assets.open("eng.traineddata")
        var fos = FileOutputStream("${context.filesDir}/tessdata/eng.traineddata")
        assets.use {
            it.copyTo(fos)
        }*/
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
    val textFolder = File("storage/emulated/0/Download/Pro Scanner/text files")
    if (!textFolder.exists()) {
        textFolder.mkdirs()
    }

    // java.io.FileNotFoundException: storage/emulated/0/Download/Pro Scanner/text_files/3pages.txt: open failed: EEXIST (File exists)
    // Handle this error when file already exists
    val textFile =
        File("storage/emulated/0/Download/Pro Scanner/text files/${nameOfTextFile.value}.txt")
    if (!textFile.exists()) {
        textFile.createNewFile()
        textFile.writeText(finalText)
    }

    showProgressDialogBoxOfTextFile.value = false
}