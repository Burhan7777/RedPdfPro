package com.pzbdownloaders.scannerfeature.components

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import java.io.File
import java.io.FileOutputStream


@Composable
fun SaveFIleAsPdf(
    showSaveDialogBox: MutableState<Boolean>,
    name: MutableState<String>,
    resultFromActivity: MutableState<GmsDocumentScanningResult?>,
    activity: MainActivity,
    viewModel: MyViewModel
) {
    if (showSaveDialogBox.value) {
        AlertDialogBox(
            name = name,
            onDismiss = { showSaveDialogBox.value = !showSaveDialogBox.value }) {
            println(resultFromActivity)
            resultFromActivity.value?.pdf?.let { pdf ->
                var externalDIr =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                var file = File("$externalDIr/Pro Scanner/Pdfs")
                if (!file.exists()) {
                    file.mkdirs()
                }
                var path =
                    File("$externalDIr/Pro Scanner/Pdfs/${name.value}.pdf")
                if (!path.exists()) {
                    path.createNewFile()
                }
                var fos = FileOutputStream(
                    path
                )
                activity.contentResolver.openInputStream(pdf.uri).use { inputStream ->
                    inputStream?.copyTo(fos)
                }
                viewModel.listOfFiles.add(path)
                viewModel.addItem()
            }
        }
    }
}