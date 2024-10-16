package com.pzbdownloaders.redpdfpro.splitpdffeature.components.componentsViewSplitPdfScreen

import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.pdfRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun UnlockPdf(
    showUnlockDialogBox: MutableState<Boolean>,
    password: MutableState<String>,
    showUnlockPdfDialogBox: MutableState<Boolean>,
    nameOfUnlockedPdf: MutableState<String>,
    path: String,
    activity: MainActivity,
    showProgress: MutableState<Boolean>,
    pathOfUnlockedFile: MutableState<String>,
    totalPagesPathFile: MutableState<Int>,
    pdfRenderer1: MutableState<PdfRenderer?>
) {
    var scope = rememberCoroutineScope()
    if (showUnlockDialogBox.value) {
        AlertDialogBox(
            name = password,
            confirmButtonText = stringResource(R.string.unlockPDF),
            id = R.string.existingPassword,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showUnlockPdfDialogBox.value = true
                }
            },
            onDismiss = { showUnlockDialogBox.value = false })
    }

    if (showUnlockPdfDialogBox.value) {
        AlertDialogBox(
            name = nameOfUnlockedPdf,
            id = R.string.saveTemporarilyAs,
            confirmButtonText = stringResource(R.string.save),
            dismissButtonText = stringResource(R.string.cancel),
            featureExecution = {
                showProgress.value = true
                scope.launch(Dispatchers.IO) {
                    val python = Python.getInstance()
                    val module = python.getModule("unlockPDFWithTempFile")
                    try {
                        var result = module.callAttr(
                            "unlock_pdf_temp_file",
                            path,
                            password.value,
                            nameOfUnlockedPdf.value
                        )

                        withContext(Dispatchers.Main) {
                            showProgress.value = false
                            if (result.toString() == "Success") {
                                val externalDir =
                                    "${
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS
                                        )
                                    }/Pro Scanner/temp"
                                pathOfUnlockedFile.value =
                                    "$externalDir/${nameOfUnlockedPdf.value}.pdf"
                                var file = File(pathOfUnlockedFile.value)
                                pdfRenderer(totalPagesPathFile, pdfRenderer1, file)
                            } else if (result.toString() == "Failure") {
                                showProgress.value = false
                                Toast.makeText(
                                    activity,
                                    "Operation failed. Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (exception: PyException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(activity, "Password is incorrect", Toast.LENGTH_SHORT)
                                .show()
                            showProgress.value = false
                        }
                    }
                }
            },
            onDismiss = {
                showUnlockPdfDialogBox.value = false
            })
    }
}