package com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.csvtopdffeature.presentation.screens

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.csvtopdffeature.presentation.components.SingleRowCsvToPdf
import com.pzbdownloaders.redpdfpro.conversionsfeature.convertToPdf.pptxtopdffeature.presentation.components.SingleRowPptxToPdf
import com.pzbdownloaders.redpdfpro.conversionsfeature.core.domain.models.InitializeJob
import com.pzbdownloaders.redpdfpro.conversionsfeature.core.domain.models.JobStatus
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.mergepdffeature.screens.scanFile
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUriForCsv
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUriForPptx
import downloadFileWithProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CsvToPdfScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    var listOfCsv = ArrayList<Uri>()

    val queryForSearch = remember { mutableStateOf("") }

    val showUploadingFIleDialogBox = remember { mutableStateOf(false) }
    val showConvertingFileDialogBox = remember { mutableStateOf(false) }
    val showDownloadingFileDialogBox = remember { mutableStateOf(false) }

    val saveAsDialogBox = remember { mutableStateOf(false) }

    val nameOfThePdfFile = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val pathOfCsvFile = remember { mutableStateOf("") }

    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                pathOfCsvFile.value = getFilePathFromContentUriForCsv(it!!, activity)!!
                saveAsDialogBox.value = true
            }
        })

    var isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            // Simulate loading or data fetching
            scope.launch(Dispatchers.Default) {
                // if (viewModel.mutableStateListOfPdfs.size == 0 && viewModel.listOfPdfNames.size == 0) {
                viewModel.mutableStateListOfCsv.clear()
                viewModel.listOfCsvNames.clear()
                getCSV(listOfCsv, activity, viewModel.listOfCsvNames, viewModel.listOfSize)
                withContext(Dispatchers.Main) {
                    viewModel.mutableStateListOfCsv = listOfCsv.toMutableStateList()
                    isRefreshing.value = false
                    //   }
                }
            }
            // stop the refresh indicator
        }
    )

    LaunchedEffect(key1 = true) {
        getCSV(
            listOfCsv,
            activity,
            viewModel.listOfCsvNames,
            viewModel.listOfSize
        )
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfCsv = listOfCsv.toMutableStateList()
        }
    }

    val filteredPdfs = remember(queryForSearch.value, viewModel.mutableStateListOfCsv) {
        if (queryForSearch.value.isBlank()) {
            viewModel.mutableStateListOfCsv // Show all files if the query is empty
        } else {
            viewModel.mutableStateListOfCsv.filterIndexed { index, _ ->
                viewModel.listOfCsvNames[index].contains(queryForSearch.value, ignoreCase = true)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        PullRefreshIndicator(
            refreshing = isRefreshing.value,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (showUploadingFIleDialogBox.value) {
            LoadingDialogBox("File is being uploaded")
        }

        if (showDownloadingFileDialogBox.value) {
            LoadingDialogBox("File is being downloaded")
        }

        if (showConvertingFileDialogBox.value) {
            LoadingDialogBox("File is being converted")
        }

        if (saveAsDialogBox.value) {
            AlertDialogBox(
                name = nameOfThePdfFile,
                featureExecution = {
                    showUploadingFIleDialogBox.value = true
                    scope.launch(Dispatchers.IO) {
                        val python = Python.getInstance()
                        val module = python.getModule("convertDocxToPDF")
                        val result = module.callAttr("make_request", pathOfCsvFile.value)
                        println(result.toString())
                        val initializeJob =
                            Gson().fromJson(result.toString(), InitializeJob::class.java)
                        showUploadingFIleDialogBox.value = false

                        //  println(initializeJob)
                        if (initializeJob.status == "initialising") {
                            showConvertingFileDialogBox.value = true
                            checkJobStatus(
                                scope,
                                module,
                                initializeJob,
                                context,
                                showConvertingFileDialogBox,
                                showDownloadingFileDialogBox,
                                navHostController,
                                nameOfThePdfFile,
                                activity
                            )
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast
                                    .makeText(context, "Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                },
                onDismiss = {
                    saveAsDialogBox.value = false
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            androidx.compose.material.OutlinedTextField(
                value = queryForSearch.value,
                onValueChange = { queryForSearch.value = it },
                label = { Text("Search CSV") },
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
                                result.launch("text/csv")
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
                                text = stringResource(id = R.string.addCsv),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                            )
                        }
                    }
                }

                itemsIndexed(items = filteredPdfs) { index, item ->
                    val originalIndex = viewModel.mutableStateListOfCsv.indexOf(item)
                    if (originalIndex != -1) {
                        SingleRowCsvToPdf(
                            uri = item,
                            nameOfCsvFile = viewModel.listOfCsvNames[originalIndex],
                            activity = activity,
                            pathOfCsvFile = pathOfCsvFile,
                            saveAsDialogBox = saveAsDialogBox
                        )
                    } else {

                    }
                }
            }
        }
    }

}

fun getCSV(
    list: ArrayList<Uri>,
    activity: MainActivity,
    listOfPptxNames: ArrayList<String>,
    listOfSizeOfFiles: ArrayList<String>
): Boolean {
    val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Files.FileColumns.SIZE
    )

    // Set the MIME type for .docx files
    val mimeType = "text/csv"
    val whereClause = MediaStore.Files.FileColumns.MIME_TYPE + " IN ('" + mimeType + "')"
    val orderBy = MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"

    val cursor: Cursor? = activity.contentResolver.query(
        MediaStore.Files.getContentUri("external"),
        projection,
        whereClause,
        null,
        orderBy
    )

    val idCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
    val nameCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
    val sizeCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

    if (cursor.moveToFirst()) {
        do {
            val fileUri = Uri.withAppendedPath(
                MediaStore.Files.getContentUri("external"),
                cursor.getString(idCol)
            )
            val name = cursor.getString(nameCol)
            list.add(fileUri)
            listOfPptxNames.add(name)

            val size = cursor.getString(sizeCol)
            if (size.toDouble() / 1000000 <= 1) {
                val sizeInKBs = String.format("%.2f KB", size.toDouble() / 1000)
                listOfSizeOfFiles.add(sizeInKBs)
            } else {
                val sizeInMBs = String.format("%.2f MB", size.toDouble() / 1000000)
                listOfSizeOfFiles.add(sizeInMBs)
            }

        } while (cursor.moveToNext())
    }
    cursor.close()
    return true
}

fun checkJobStatus(
    scope: CoroutineScope,
    module: PyObject,
    initializeJob: InitializeJob,
    context: Context,
    showConvertingFileDialogBox: MutableState<Boolean>,
    showDownloadingFIleDialogBox: MutableState<Boolean>,
    navHostController: NavHostController,
    nameOfFile: MutableState<String>,
    activity: MainActivity
) {
    val path =
        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/Pro Scanner/Pdfs/${nameOfFile.value}.pdf"
    scope.launch(Dispatchers.IO) {
        delay(3000)
        val result = module.callAttr("check_status", initializeJob.id)
        val jobStatus = Gson().fromJson(result.toString(), JobStatus::class.java)
        if (jobStatus.status == "successful") {
            showConvertingFileDialogBox.value = false
            showDownloadingFIleDialogBox.value = true
            val downloadFileStatus =
                downloadFileWithProgress(jobStatus.target_files[0].id.toInt(), path, context) { progress ->

                }
            withContext(Dispatchers.Main) {
                if (downloadFileStatus == "Success") {

                    showDownloadingFIleDialogBox.value = false
                    scanFile(path, activity)
                    navHostController.navigate(
                        Screens.FinalScreenOfPdfOperations.finalScreen(
                            path = path,
                            uri = path,
                            ""
                        )
                    )
                } else {
                    showDownloadingFIleDialogBox.value = false
                    Toast.makeText(context, "File conversion failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            checkJobStatus(
                scope,
                module,
                initializeJob,
                context,
                showConvertingFileDialogBox,
                showDownloadingFIleDialogBox,
                navHostController,
                nameOfFile,
                activity
            )
        }
    }
}



