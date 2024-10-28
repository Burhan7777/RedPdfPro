package com.pzbdownloaders.redpdfpro.extractimagefeature.util

import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.DisplayMessageDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

@Composable
fun UnlockPdfAndExtractImages(
    showExtractingImagesLoadingBox: MutableState<Boolean>,
    showFileIsLockedDialogBox: MutableState<Boolean>,
    showUnlockDialogBox: MutableState<Boolean>,
    password: MutableState<String>,
    showSaveAsTempAndExtractImages: MutableState<Boolean>,
    showProgressOfUnlocking: MutableState<Boolean>,
    pathOfFile: MutableState<String>,
    pathOfTempFile: MutableState<String>,
    activity: MainActivity,
    navHostController: NavHostController,
    viewModel: MyViewModel
) {

    val scope = rememberCoroutineScope()
    if (showExtractingImagesLoadingBox.value) {
        LoadingDialogBox("Extracting images from PDF")
    }

    if (showFileIsLockedDialogBox.value) {
        DisplayMessageDialogBox(
            "This file is locked. You can unlock it and then add it.",
            confirmTextButtonText = "Unlock",
            cancelTextButtonText = "Cancel",
            featureExecution = {
                showUnlockDialogBox.value = true
            }
        ) {
            showFileIsLockedDialogBox.value = false
        }
    }

    if (showUnlockDialogBox.value) {
        AlertDialogBox(
            name = password,
            confirmButtonText = stringResource(R.string.unlockPDF),
            id = R.string.existingPassword,
            featureExecution = {
                showSaveAsTempAndExtractImages.value = true
            },
            onDismiss = { showUnlockDialogBox.value = false })
    }

    if (showProgressOfUnlocking.value) {
        LoadingDialogBox("Unlocking PDF")
    }

    if (showSaveAsTempAndExtractImages.value) {
        LaunchedEffect(true) {
            val randomUUID = UUID.randomUUID()
            val tempNameOfFile = "$randomUUID"
            showProgressOfUnlocking.value = true
            scope.launch(Dispatchers.IO) {
                val python = Python.getInstance()
                val module = python.getModule("unlockPDFWithTempFile")
                try {
                    var result = module.callAttr(
                        "unlock_pdf_temp_file",
                        pathOfFile.value,
                        password.value,
                        tempNameOfFile
                    )

                    withContext(Dispatchers.Main) {
                        if (result.toString() == "Success") {
                            showProgressOfUnlocking.value = false
                            val externalDir =
                                "${
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS
                                    )
                                }/Pro Scanner/temp"
                            pathOfTempFile.value =
                                "$externalDir/${tempNameOfFile}.pdf"
                            extractImagesFromPDFWithPDFBoxAndroid(
                                File(pathOfTempFile.value),
                                activity,
                                scope,
                                showExtractingImagesLoadingBox,
                                navHostController,
                                viewModel,
                                showSaveAsTempAndExtractImages,
                                pathOfTempFile
                            )

                        } else if (result.toString().contains("Failure")) {
                            Toast.makeText(
                                activity,
                                "Operation failed",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            showProgressOfUnlocking.value = false
                        } else {
                            showProgressOfUnlocking.value = false
                        }
                    }
                } catch (exception: PyException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            activity,
                            "Password is incorrect",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        showProgressOfUnlocking.value = false
                    }
                }
            }
        }
    }
}