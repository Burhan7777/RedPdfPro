package com.pzbdownloaders.redpdfpro.mergepdffeature.screens

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.DisplayMessageDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.mergepdffeature.components.SingleRowMergePdf
import com.pzbdownloaders.redpdfpro.mergepdffeature.components.SingleRowSelectedPdfs
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.scannerfeature.util.scanFile
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRowSplitFeature
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.pdfRenderer
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MergePdf(
    activity: MainActivity,
    viewModel: MyViewModel,
    fileName: String?,
    filePath: String?,
    navHostController: NavHostController
) {

    var showAlertBox = remember {
        mutableStateOf(false)
    }
    var listOfPdfs = ArrayList<Uri>()

    val queryForSearch = remember { mutableStateOf("") }

    var showFileIsLockedDialogBox = remember { mutableStateOf(false) }

    var showEnterPasswordDialogBox = remember { mutableStateOf(false) }

    var showSaveAsTemp = remember { mutableStateOf(false) }

    var showProgressOfUnlocking = remember { mutableStateOf(false) }

    var password = remember { mutableStateOf("") }

    var tempNameOfFile = remember { mutableStateOf("") }

    var pathOfLockedFile = remember { mutableStateOf("") }

    var pathOfTempFIle = remember { mutableStateOf("") }


    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                viewModel.pdfNames.add(getFileName(it, activity))
                viewModel.listOfPdfToMerge.add(getFilePathFromContentUri(it, activity = activity)!!)
            }
        })

    BackHandler {
        viewModel.pdfNames.clear()
        viewModel.listOfPdfToMerge.clear()
        navHostController.navigateUp()
    }

    LaunchedEffect(key1 = true) {
        if (fileName != "")
            viewModel.pdfNames.add(fileName!!)

        if (filePath != "")
            viewModel.listOfPdfToMerge.add(filePath!!)
    }

    var name = remember {
        mutableStateOf("")
    }

    var showProgress by remember {
        mutableStateOf(false)
    }

    var scope1 = rememberCoroutineScope()
    var isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            // Simulate loading or data fetching
            scope1.launch(Dispatchers.Default) {
                // if (viewModel.mutableStateListOfPdfs.size == 0 && viewModel.listOfPdfNames.size == 0) {
                viewModel.mutableStateListOfPdfs.clear()
                viewModel.listOfPdfNames.clear()
                getPdfs(listOfPdfs, activity, viewModel.listOfPdfNames, viewModel.listOfSize)
                withContext(Dispatchers.Main) {
                    viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
                    isRefreshing.value = false
                    //   }
                }
            }
            // stop the refresh indicator
        }
    )

    var context = LocalContext.current
    var scope = rememberCoroutineScope()

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    LaunchedEffect(key1 = true) {
        getPdfs(
            listOfPdfs,
            activity,
            viewModel.listOfPdfNames,
            viewModel.listOfSize
        )
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
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

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {

        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(10.dp)

            ) {
//                items(count = viewModel.pdfNames.size) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(50.dp)
//                            .border(
//                                width = 1.dp,
//                                shape = RoundedCornerShape(10.dp),
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                            .clip(shape = RoundedCornerShape(10.dp))
//                            .background(MaterialTheme.colorScheme.primary),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = " ${viewModel.pdfNames[it]}",
//                            color = MaterialTheme.colorScheme.onPrimary,
//                            modifier = Modifier.padding(start = 20.dp),
//                            overflow = TextOverflow.Ellipsis
//                        )
//
//                    }
//                }
                itemsIndexed(items = viewModel.listOfPdfToMerge) { index, item ->
                    SingleRowSelectedPdfs(
                        path = item,
                        pdfName = viewModel.pdfNames[index],
                        viewModel = viewModel,
                        index = index
                    )
                }
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(start = 20.dp, end = 20.dp)
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
                        SingleRowMergePdf(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            navHostController = navHostController,
                            viewModel = viewModel,
                            index = index,
                            viewModel.listOfPdfToMerge,
                            showFileIsLockedDialogBox,
                            pathOfLockedFile,
                        )
                    } else {

                    }
                }
            }
        }

        if (showProgress) {
            LoadingDialogBox("Files are being merged")
        }
        if (viewModel.listOfPdfToMerge.size > 1) {
            Button(
                onClick = { showAlertBox.value = !showAlertBox.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(id = R.string.mergePDFs))
            }
        }
        if (showAlertBox.value) {
            AlertDialogBox(
                name = name,
                onDismiss = { showAlertBox.value = !showAlertBox.value }) {
                lateinit var result: PyObject
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("mergePDF")
                    result = module.callAttr(
                        "merge_pdf",
                        viewModel.listOfPdfToMerge.toTypedArray(),
                        name.value
                    )
                    withContext(Dispatchers.Main) {
                        if (result.toString() == "Success") {
                            showProgress = false
                            Toast.makeText(
                                context,
                                "Successfully saved",
                                Toast.LENGTH_SHORT
                            ).show()
                            val externalDIr =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            val path = "${externalDIr}/Pro Scanner/Pdfs/${name.value}.pdf"
                            scanFile(path, activity)
                            navHostController.navigate(
                                Screens.FinalScreenOfPdfOperations.finalScreen(
                                    "$externalDIr/Pro Scanner/Pdfs/${name.value}.pdf",
                                    "$externalDIr/Pro Scanner/Pdfs/${name.value}.pdf",
                                    pathOfUnlockedFile = pathOfTempFIle.value
                                )
                            )
                        } else if (result.toString() == "Failure") {
                            showProgress = false
                            Toast.makeText(context, "Operation Failed. Please try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
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
                id = R.string.saveTemporarilyAs,
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
                                pathOfLockedFile.value,
                                password.value,
                                tempNameOfFile.value
                            )

                            withContext(Dispatchers.Main) {
                                showProgressOfUnlocking.value = false
                                if (result.toString() == "Success") {
                                    val externalDir =
                                        "${
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_DOWNLOADS
                                            )
                                        }/Pro Scanner/temp"
                                    pathOfTempFIle.value =
                                        "$externalDir/${tempNameOfFile.value}.pdf"
                                    viewModel.listOfPdfToMerge.add(pathOfTempFIle.value)
                                    viewModel.pdfNames.add(tempNameOfFile.value)
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
            LoadingDialogBox("PDF is being unlocked")
        }
    }
}

fun scanFile(filePath: String, context: Context) {
    MediaScannerConnection.scanFile(
        context,
        arrayOf(filePath),
        null
    ) { path, uri ->
        // Callback invoked after scanning is complete
        // You can perform any additional actions here if needed
    }
}