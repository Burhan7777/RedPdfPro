package com.pzbdownloaders.redpdfpro.unlockpdffeature

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.DisplayMessageDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.scanFile
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRowSplitFeature
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.unlockpdffeature.components.SingleRowUnlockScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UnlockPdf(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    var showProgress by remember {
        mutableStateOf(false)
    }

    var listOfPdfs = ArrayList<Uri>()

    val showAlertBox = remember { mutableStateOf(false) }

    val showPasswordDialogBox = remember { mutableStateOf(false) }

    val password = remember { mutableStateOf("") }

    val name = remember { mutableStateOf("") }

    val path = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var queryForSearch = remember { mutableStateOf("") }

    val context = LocalContext.current

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

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                path.value = getFilePathFromContentUri(it, activity)!!
                showPasswordDialogBox.value = true
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
    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {

        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

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
                        SingleRowUnlockScreen(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            navHostController = navHostController,
                            viewModel = viewModel,
                            showDialogBox = showPasswordDialogBox,
                            path = path

                        )
                    } else {

                    }
                }
            }
        }
    }
    Box(contentAlignment = Alignment.Center) {
        if (showProgress) {
            LoadingDialogBox("PDF is being unlocked")
        }
    }
    if (showPasswordDialogBox.value) {
        val python = Python.getInstance()
        val module = python.getModule("checkLockStatus")
        var result = module.callAttr(
            "check_lock_status_pdf",
            path.value
        )
        if (result.toString() == "Unlocked") {
            DisplayMessageDialogBox(
                message = "This file is already unlocked",
                confirmTextButtonText = "OK",
                cancelTextButtonText = "Cancel"
            ) {
                showPasswordDialogBox.value = false
            }
        } else {
            AlertDialogBox(
                name = password,
                id = R.string.existingPassword,
                featureExecution = {
                    scope.launch(Dispatchers.IO) {
                        showAlertBox.value = !showAlertBox.value
                    }
                },
                onDismiss = { showPasswordDialogBox.value = false })
        }
    }

    if (showAlertBox.value) {
        AlertDialogBox(
            name = name,
            id = R.string.enterFileName,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("unlockPDF")
                    try {
                        var result = module.callAttr(
                            "unlock_pdf",
                            path.value,
                            password.value,
                            name.value
                        )
                        withContext(Dispatchers.Main) {
                            if (result.toString() == "Success") {
                                withContext(Dispatchers.Main) {
                                    showProgress = false
                                }
                                val externalDir =
                                    "${
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS
                                        )
                                    }/Pro Scanner/Pdfs"
                                viewModel.listOfFiles.add(File("$externalDir/${name.value}.pdf"))
                                scanFile("$externalDir/${name.value}.pdf", activity)
                                navHostController.navigate(
                                    Screens.FinalScreenOfPdfOperations.finalScreen(
                                        "$externalDir/${name.value}.pdf",
                                        "$externalDir/${name.value}.pdf",
                                        pathOfUnlockedFile = ""
                                    )
                                )
                            } else if (result.toString() == "Failure") {
                                showProgress = false
                                Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } catch (exception: PyException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Password entered is wrong", Toast.LENGTH_SHORT)
                                .show()
                            showProgress = false
                        }
                    }
                }
            },
            onDismiss = { showAlertBox.value = false })
    }
}