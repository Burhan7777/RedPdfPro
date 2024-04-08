package com.pzbdownloaders.redpdfpro.scannerfeature.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox

@Composable
fun SavePdfAsImage(showProgressDialogBox: MutableState<Boolean>, message: MutableState<String>) {
    if (showProgressDialogBox.value) {
        ProgressDialogBox(message = message)
    }
}