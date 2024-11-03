package com.pzbdownloaders.redpdfpro.splitpdffeature.screens

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRow
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRowSplitFeature
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SplitPdf(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MyViewModel,
    filePath: String?
) {

    var scope = rememberCoroutineScope()
    var listOfPdfs = ArrayList<Uri>()
    var path by remember {
        mutableStateOf(" ")
    }

    var isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            // Simulate loading or data fetching
            scope.launch(Dispatchers.Default) {
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


    BackHandler {
        viewModel.mutableStateListOfPdfs.clear()
        viewModel.listOfPdfNames.clear()
        navHostController.popBackStack()
    }

    val queryForSearch = remember { mutableStateOf("") }
    val searchActiveBoolean = remember { mutableStateOf(false) }

    var searchResultList = remember { mutableStateListOf<Uri>() }


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


    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                path = getFilePathFromContentUri(it, activity)!!
                viewModel.modelList.clear()
                navHostController.navigate(
                    Screens.ViewSplitPdfScreen.viewSplitPdfScreen(
                        path,
                        it.toString()
                    )
                )
                /*      val python = Python.getInstance()
                      val module = python.getModule("splitPDF")
                      var response = module.callAttr("is_encrypted", path)
                      if (response.toString() == "True") {
                          showPasswordAlertBox.value = true
                          println(passwordOfLockedFile.value)
                          if (passwordOfLockedFile.value.isNotEmpty()) {
                              totalPages =
                                  module.callAttr("total_pages", path, passwordOfLockedFile.value)
                                      .toString()
                                      .toInt()
                          }
                      } else if (response.toString() == "False") {
                          totalPages =
                              module.callAttr("total_pages", path).toString()
                                  .toInt()
                      }*/
                /*   if (response== "PDFlocked") {
                       showPasswordAlertBox.value = true
                       totalPages =
                           module.callAttr("unlock_pdf", path, passwordOfLockedFile.value).toString()
                               .toInt()
                   } else {
                       totalPages = response.toString().toInt()*/
                // }
            }
        })

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {

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
                        SingleRowSplitFeature(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            navHostController = navHostController,
                            viewModel = viewModel
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
}


fun getPdfs(
    list: ArrayList<Uri>,
    activity: MainActivity,
    listOfPdfNames: ArrayList<String>,
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

    val mimeType = "application/pdf"

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
    val mimeCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
    val addedCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
    val modifiedCol =
        cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)
    val nameCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
    val titleCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
    val sizeCol = cursor!!.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

    if (cursor!!.moveToFirst()) {
        do {
            val fileUri = Uri.withAppendedPath(
                MediaStore.Files.getContentUri("external"),
                cursor!!.getString(idCol)
            )
            val name = cursor.getString(titleCol)
            list.add(fileUri)
            listOfPdfNames.add(name)
            val size = cursor.getString(sizeCol)
            if (size.toDouble() / 1000000 <= 1) {
                val floatValue = size.toDouble() / 1000
                val sizeInKBs: Double = String.format("%.2f", floatValue).toDouble()
                listOfSizeOfFiles.add("$sizeInKBs KB")
            } else if (size.toDouble() / 1000000 > 1) {
                val floatValue = size.toDouble() / 1000000
                val sizeInMbs: Double = String.format("%.2f", floatValue).toDouble()
                listOfSizeOfFiles.add("$sizeInMbs MB")
            }

            // println(fileUri)
            val mimeType = cursor!!.getString(mimeCol)
            val dateAdded = cursor!!.getLong(addedCol)
            val dateModified = cursor!!.getLong(modifiedCol)
            // ...
        } while (cursor!!.moveToNext())
    }
    return true

}
