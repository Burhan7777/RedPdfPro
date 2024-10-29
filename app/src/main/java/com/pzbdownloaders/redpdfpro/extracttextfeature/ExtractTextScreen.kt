package com.pzbdownloaders.redpdfpro.extracttextfeature

import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.DisplayMessageDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.extracttextfeature.components.SingleRowExtractText
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ExtractText(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    var path = remember { mutableStateOf("") }
    var nameOfFile = remember { mutableStateOf("") }
    var name = remember {
        mutableStateOf("")
    }

    var listOfPdfs = ArrayList<Uri>()
    var alertDialogBox = remember {
        mutableStateOf(false)
    }

    var showProgress by remember {
        mutableStateOf(false)
    }

    var queryForSearch = remember { mutableStateOf("") }

    var showFileIsLockedDialogBox = remember { mutableStateOf(false) }

    var showEnterPasswordDialogBox = remember { mutableStateOf(false) }

    var showSaveAsTemp = remember { mutableStateOf(false) }

    var password = remember { mutableStateOf("") }

    var tempNameOfFile = remember { mutableStateOf("") }

    var showProgressOfUnlocking = remember { mutableStateOf(false) }

    var pathOfLockedFile = remember { mutableStateOf("") }

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )


    var context = LocalContext.current

    var scope = rememberCoroutineScope()

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                nameOfFile.value = getFileName(it, activity)
                path.value = getFilePathFromContentUri(it, activity)!!
                val pythonLockedStatus = Python.getInstance()
                val moduleLockedStatus = pythonLockedStatus.getModule("checkLockStatus")
                var lockedResult = moduleLockedStatus.callAttr("check_lock_status_pdf", path.value)
                if (lockedResult.toString() == "Locked") {
                    showFileIsLockedDialogBox.value = true
                } else {
                    alertDialogBox.value = true
                }
            }

        })

    LaunchedEffect(key1 = true) {
        getPdfs(
            listOfPdfs,
            activity,
            viewModel.listOfPdfNames,
            viewModel.listOfSize
        )
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
            println("QUERY:${viewModel.mutableStateListOfPdfs[0].host}")
        }
    }

    val filteredPdfs = remember(queryForSearch.value, viewModel.mutableStateListOfPdfs) {
        if (queryForSearch.value.isBlank()) {
            viewModel.mutableStateListOfPdfs // Show all files if the query is empty
        } else {
            viewModel.mutableStateListOfPdfs.filterIndexed { index, _ ->
                viewModel.listOfPdfNames[index].contains(queryForSearch.value, ignoreCase = true)
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            androidx.compose.material.OutlinedTextField(
                value = queryForSearch.value,
                onValueChange = { queryForSearch.value = it },
                label = { Text("Search PDFs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(10.dp),
                    topEnd = CornerSize(10.dp),
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp),
                )
            )


            LazyColumn() {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .clickable {
                                result.launch("application/pdf")
                            }
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.Red,
                                    style = stroke,
                                    cornerRadius = CornerRadius(10.dp.toPx())
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.upload),
                                contentDescription = stringResource(
                                    id = R.string.upload
                                ),
                                modifier = Modifier
                                    .padding(top = 20.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.addPDF),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                            )
                        }
                    }
                }

                itemsIndexed(items = filteredPdfs) { index, item ->
                    val originalIndex = viewModel.mutableStateListOfPdfs.indexOf(item)
                    if (originalIndex != -1) {
                        SingleRowExtractText(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            path = path,
                            alertDialogBox = alertDialogBox,
                            showFileIsLockedDialogBox = showFileIsLockedDialogBox
                        )
                    } else {

                    }
                }
//                itemsIndexed(items = viewModel.mutableStateListOfPdfs) { index, item ->
//
//                    SingleRowSplitFeature(
//                        uri = item,
//                        nameOfPdfFile = viewModel.listOfPdfNames[index],
//                        activity = activity,
//                        navHostController = navHostController,
//                        viewModel = viewModel
//                    )
//                }
            }
        }
    }
    if (showProgress) {
        LoadingDialogBox("Text is being extracted")
    }
    if (showFileIsLockedDialogBox.value) {
        DisplayMessageDialogBox(
            "This file is locked. You can unlock it and then add it.",
            confirmTextButtonText = "Unlock",
            cancelTextButtonText = "Cancel",
            featureExecution = {
                showEnterPasswordDialogBox.value = true
            }
        ) {
            showFileIsLockedDialogBox.value = false
        }
    }
    if (showEnterPasswordDialogBox.value) {
        AlertDialogBox(
            name = password,
            confirmButtonText = stringResource(R.string.unlockPDF),
            id = R.string.existingPassword,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showSaveAsTemp.value = true
                }
            },
            onDismiss = { showEnterPasswordDialogBox.value = false })
    }
    if (showSaveAsTemp.value) {
        AlertDialogBox(
            name = tempNameOfFile,
            id = R.string.saveAs,
            confirmButtonText = stringResource(R.string.save),
            dismissButtonText = stringResource(R.string.cancel),
            featureExecution = {
                showProgressOfUnlocking.value = true
                scope.launch(Dispatchers.IO) {
                    val python = Python.getInstance()
                    val module = python.getModule("unlockPDFWithTempFile")
                    try {
                        var result = module.callAttr(
                            "unlock_pdf_temp_file",
                            path.value,
                            password.value,
                            tempNameOfFile.value
                        )

                        withContext(Dispatchers.Main) {
                            if (result.toString() == "Success") {
                                val externalDir =
                                    "${
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS
                                        )
                                    }/Pro Scanner/temp"
                                path.value =
                                    "$externalDir/${tempNameOfFile.value}.pdf"
                                withContext(Dispatchers.IO) {
                                    val python = Python.getInstance()
                                    val module = python.getModule("extractTextPDF")
                                    var result = module.callAttr(
                                        "extract_text_pypdf",
                                        path.value,
                                        tempNameOfFile.value
                                    )
                                }
                                showProgressOfUnlocking.value = false
                                withContext(Dispatchers.Main) {
                                    val externalDir1 =
                                        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}"
                                    showProgress = false
                                    if (result.toString() == "Success") {
                                        navHostController.navigate(
                                            Screens.FinalScreenOfTextExtraction.withParameters(
                                                "${externalDir1}/Pro Scanner/text files/${tempNameOfFile.value}.txt"
                                            )
                                        )

                                    } else if (result.toString() == "Failure") {
                                        showProgress = false
                                        Toast.makeText(
                                            context,
                                            "Operation failed",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            } else if (result.toString() == "Failure") {
                                showProgressOfUnlocking.value = false
                                Toast.makeText(
                                    activity,
                                    "Operation failed. Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

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
            },
            onDismiss = {
                showSaveAsTemp.value = false
            })
    }
    if (showProgressOfUnlocking.value) {
        LoadingDialogBox("Saving file")
    }
    if (alertDialogBox.value) {
        val externalDir =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}"
        AlertDialogBox(name = name, onDismiss = { alertDialogBox.value = !alertDialogBox.value }) {
            scope.launch(Dispatchers.IO) {
                showProgress = true
                val python = Python.getInstance()
                val module = python.getModule("extractTextPDF")
                var result = module.callAttr("extract_text_pypdf", path.value, name.value)
                withContext(Dispatchers.Main) {
                    showProgress = false
                    if (result.toString() == "Success") {
                        navHostController.navigate(
                            Screens.FinalScreenOfTextExtraction.withParameters(
                                "${externalDir}/Pro Scanner/text files/${name.value}.txt"
                            )
                        )
                    } else if (result.toString() == "Failure") {
                        showProgress = false
                        Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}