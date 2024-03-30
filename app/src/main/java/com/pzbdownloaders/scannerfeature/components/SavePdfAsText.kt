package com.pzbdownloaders.scannerfeature.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel

@Composable
fun SavePdfAsText(
    nameOfTextFile: MutableState<String>,
    showTextFileSaveDialogBox: MutableState<Boolean>,
    viewModel: MyViewModel,
    activity: MainActivity,
    pathOfPdfFile: MutableState<String>,
    messageSavingTextFIle: MutableState<String>
) {

    if (showTextFileSaveDialogBox.value) {
        AlertDialogBox(
            name = nameOfTextFile,
            id = R.string.saveAsTxtFile,
            onDismiss = { showTextFileSaveDialogBox.value = false }) {
            viewModel.convertPdfIntoTextFile(activity, pathOfPdfFile.value, nameOfTextFile)
            viewModel.showProgressDialogBoxOfTextFile.value = true
        }
    }
    if (viewModel.showProgressDialogBoxOfTextFile.value) {
        ProgressDialogBox(message = messageSavingTextFIle)
    }
}