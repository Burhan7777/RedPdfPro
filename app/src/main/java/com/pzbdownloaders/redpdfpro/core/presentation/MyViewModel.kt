package com.pzbdownloaders.redpdfpro.core.presentation

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import com.pzbdownloaders.scannerfeature.util.ScannerModel
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

    fun getImage() {
        viewModelScope.launch(Dispatchers.Default) {
            if ((listOfFiles.size ?: 0) > modelScanner.size) {
                for (i in 0 until (listOfFiles.size ?: 0)) {
                    modelScanner.add(
                        ScannerModel(
                            mutableStateOf(listOfFiles[i].name),
                            mutableStateOf(null)
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
                var pdfRenderer = PdfRenderer(parcelFileDescriptor)
                var bitmap = loadPage(
                    0,
                    pdfRenderer
                )
                withContext(Dispatchers.Main) {
                    modelScanner[i] = ScannerModel(
                        mutableStateOf(listOfFiles[i].name),
                        mutableStateOf(bitmap)
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
                        mutableStateOf(bitmap)
                    )
                )
                var lastModelScanner = modelScanner[modelScanner.size - 1]
                for (i in modelScanner.size downTo  0) {
                    try {
                        modelScanner[i] = modelScanner[i - 1]
                    } catch (exception: IndexOutOfBoundsException) {

                    }
                }
                modelScanner[0] = lastModelScanner
            }
        }
    }
}