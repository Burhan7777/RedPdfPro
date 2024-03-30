package com.pzbdownloaders.redpdfpro.core.presentation

import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import com.pzbdownloaders.scannerfeature.util.ScannerModel
import com.pzbdownloaders.scannerfeature.util.convertPdfToImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MyViewModel : ViewModel() {
    var modelList = mutableStateListOf<modelBitmap>()
    var listOfPdfToMerge = mutableStateListOf<String>()
    var pdfNames = mutableStateListOf<String>()
    var modelScanner: SnapshotStateList<ScannerModel> = mutableStateListOf()
    var listOfFiles: ArrayList<File> = ArrayList()
    val showProgressDialogBoxOfWordFile = mutableStateOf(false)

    fun getImage() {
        viewModelScope.launch(Dispatchers.Default) {
            if ((listOfFiles.size ?: 0) > modelScanner.size) {
                for (i in 0 until (listOfFiles.size ?: 0)) {
                    modelScanner.add(
                        ScannerModel(
                            mutableStateOf(listOfFiles[i].name),
                            mutableStateOf(null),
                            "storage/emulated/0/Download/Pro Scanner/Pdfs/${listOfFiles[i].name}"
                        )
                    )

                }
            }

            for (i in 0 until listOfFiles.size) {
                var parcelFileDescriptor =
                    ParcelFileDescriptor.open(
                        listOfFiles[i],
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )

                //Process: com.pzbdownloaders.redpdfpro, PID: 7895
                //                                                                                                    java.io.IOException: file not in PDF format or corrupted
                //
                //catch this exception in pro scanner while creating bitmap

                var pdfRenderer = PdfRenderer(parcelFileDescriptor)
                var bitmap = loadPage(
                    0,
                    pdfRenderer
                )
                withContext(Dispatchers.Main) {
                    modelScanner[i] = ScannerModel(
                        mutableStateOf(listOfFiles[i].name),
                        mutableStateOf(bitmap),
                        "storage/emulated/0/Download/Pro Scanner/Pdfs/${listOfFiles[i].name}"
                    )
                }
            }
        }
    }

    fun addItem() {
        viewModelScope.launch(Dispatchers.Default) {
            var parcelFileDescriptor =
                ParcelFileDescriptor.open(
                    listOfFiles[listOfFiles.size - 1],
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
            var pdfRenderer = PdfRenderer(parcelFileDescriptor)
            var bitmap = loadPage(
                0,
                pdfRenderer
            )
            withContext(Dispatchers.Main) {
                modelScanner.add(
                    ScannerModel(
                        mutableStateOf(listOfFiles[listOfFiles.size - 1].name),
                        mutableStateOf(bitmap),
                        "storage/emulated/0/Download/Pro Scanner/Pdfs/${listOfFiles[listOfFiles.size - 1].name}"
                    )
                )
                var lastModelScanner = modelScanner[modelScanner.size - 1]
                for (i in modelScanner.size downTo 0) {
                    try {
                        modelScanner[i] = modelScanner[i - 1]
                    } catch (exception: IndexOutOfBoundsException) {

                    }
                }
                modelScanner[0] = lastModelScanner
            }
        }
    }

    fun convertPdfIntoAWordFIle(
        context: Context,
        path: String,
        nameOfWordFile: MutableState<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            convertPdfToImage(context, path, nameOfWordFile,showProgressDialogBoxOfWordFile)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
}