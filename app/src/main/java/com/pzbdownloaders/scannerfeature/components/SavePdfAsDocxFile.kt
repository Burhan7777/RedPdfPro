package com.pzbdownloaders.scannerfeature.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel

@Composable
fun SavePdfAsDocxFile(
    showWordFIleSaveDialogBox: MutableState<Boolean>,
    nameOfWordFile: MutableState<String>,
    viewModel: MyViewModel,
    activity: MainActivity,
    pathOfPdfFile: MutableState<String>,
    messageSavingWordFIle: MutableState<String>
) {
    if (showWordFIleSaveDialogBox.value) {
        AlertDialogBox(
            name = nameOfWordFile,
            onDismiss = {
                showWordFIleSaveDialogBox.value = !showWordFIleSaveDialogBox.value
            }) {
            viewModel.showProgressDialogBoxOfWordFile.value = true
            viewModel.convertPdfIntoAWordFIle(
                activity.applicationContext,
                pathOfPdfFile.value,
                nameOfWordFile
            )
        }
    }
    if (viewModel.showProgressDialogBoxOfWordFile.value) {
        ProgressDialogBox(message = messageSavingWordFIle)
    }
}