package com.pzbdownloaders.redpdfpro.core.presentation

import android.app.Application
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.pzbdownloaders.redpdfpro.scannerfeature.util.ScannerModel
import com.pzbdownloaders.redpdfpro.scannerfeature.util.convertPdfToImage
import com.pzbdownloaders.redpdfpro.scannerfeature.util.getImagesForTextFiles
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
    var mutableStateListOfPdfs = mutableStateListOf<Uri>()
    var listOfPdfNames = ArrayList<String>()
    var listOfSize = ArrayList<String>()
    var listOfImagesFromExtractImages = ArrayList<String>()
    var mutableStateListOfDocx = mutableStateListOf<Uri>()
    var listOfDocxNames = ArrayList<String>()
    var mutableStateListOfPptx= mutableStateListOf<Uri>()
    var listOfPptxNames = ArrayList<String>()
    var mutableStateListOfXls= mutableStateListOf<Uri>()
    var listOfXlsNames = ArrayList<String>()
    val showProgressDialogBoxOfWordFile = mutableStateOf(false)
    val showProgressDialogBoxOfTextFile = mutableStateOf(false)

    fun getImage() {
        viewModelScope.launch(Dispatchers.Default) {
            if ((listOfFiles.size ?: 0) > modelScanner.size) {
                for (i in 0 until (listOfFiles.size ?: 0)) {
                    modelScanner.add(
                        ScannerModel(
                            listOfFiles[i].name,
                            null,
                            listOfFiles[i].absolutePath
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
                        listOfFiles[i].name,
                        bitmap,
                        listOfFiles[i].absolutePath
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
                        listOfFiles[listOfFiles.size - 1].name,
                        bitmap,
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
            convertPdfToImage(
                context,
                path,
                nameOfWordFile,
                showProgressDialogBoxOfWordFile,
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun convertPdfIntoTextFile(
        context: Context,
        path: String,
        nameOfTextFile: MutableState<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getImagesForTextFiles(
                context,
                path,
                nameOfTextFile,
                showProgressDialogBoxOfTextFile,
            )
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

}