package com.pzbdownloaders.redpdfpro.documentfeature.screens

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.documentfeature.components.SingleRowDocumentFeature
import com.pzbdownloaders.redpdfpro.documentfeature.util.ShareAsPdfOrImage
import com.pzbdownloaders.redpdfpro.scannerfeature.components.BottomSheet.BottomSheet
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsImage
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DocumentFeature(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    var listOfPdfs = ArrayList<Uri>()
    var scope = rememberCoroutineScope()
    val showProgressBarOfPdfSavedAsImage = remember { mutableStateOf(false) }
    val nameOfTheWordFile = remember { mutableStateOf("") }
    val saveWordFIleDialogBox = remember { mutableStateOf(false) }
    val pathOfThePdfFile = remember { mutableStateOf("") }
    val showBottomSheet = remember { mutableStateOf(false) }
    val showDeleteDialogBox = remember { mutableStateOf(false) }
    val showPasswordDialogBox = remember { mutableStateOf(false) }
    val showSaveDialogBox = remember { mutableStateOf(false) }
    val nameOfThePdfFile = remember { mutableStateOf<String?>("") }
    val uriOfFile = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val showShareDialogBox = remember { mutableStateOf(false) }
    val shareFIleAsPdf = remember { mutableStateOf(false) }
    val shareFileAsImages = remember { mutableStateOf(false) }
    var currentUri = remember { mutableStateOf<Uri?>(null) }
    val showConvertingIntoImagesProgressDialogBox = remember { mutableStateOf(false) }
    val queryForSearch = remember { mutableStateOf("") }
    val searchActiveBoolean = remember { mutableStateOf(false) }
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

    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        if (viewModel.mutableStateListOfPdfs.size == 0 && viewModel.listOfPdfNames.size == 0) {
            getPdfs(listOfPdfs, activity, viewModel.listOfPdfNames, viewModel.listOfSize)
            withContext(Dispatchers.Main) {
                viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
            }
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


    //  println(viewModel.mutableStateListOfPdfs.size)
//    println(viewModel.listOfPdfNames.size)

    viewModel.modelList.clear()

    SavePdfAsImage(
        showProgressDialogBox = showProgressBarOfPdfSavedAsImage,
        message = remember { mutableStateOf("Save pdf as image") }
    )
    SavePdfAsDocxFile(
        showWordFIleSaveDialogBox = saveWordFIleDialogBox,
        nameOfWordFile = nameOfTheWordFile,
        viewModel = viewModel,
        activity = activity,
        pathOfPdfFile = pathOfThePdfFile,
        messageSavingWordFIle = remember { mutableStateOf("Saving pdf as .docx") },
    )

    BottomSheet(
        showBottomSheet = showBottomSheet,
        showDeleteDialogBox = showDeleteDialogBox,
        viewModel = viewModel,
        activity = activity,
        navHostController = navHostController,
        pathOfPdfFile = pathOfThePdfFile,
        nameOfPdfFIle = nameOfThePdfFile,
        showPasswordDialogBox = showPasswordDialogBox,
        showSaveAsLockPdfBox = showSaveDialogBox,
        uriOfFile = uriOfFile,
        listOfPdfs = viewModel.mutableStateListOfPdfs
    )


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

        Column(modifier = Modifier.fillMaxSize()) {
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


            if (showShareDialogBox.value) {
                ShareAsPdfOrImage(
                    shareFIleAsPdf = shareFIleAsPdf,
                    shareFileAsImages = shareFileAsImages,
                    showShareDialogBox = showShareDialogBox
                )
            }
            if (showConvertingIntoImagesProgressDialogBox.value) {
                ProgressDialogBox(message = mutableStateOf(stringResource(id = R.string.convertingIntoImages)))
            }
            LazyColumn(state = lazyListState) {
                itemsIndexed(items = filteredPdfs) { index, item ->
                    val originalIndex = viewModel.mutableStateListOfPdfs.indexOf(item)
                    if (originalIndex != -1) {
                        SingleRowDocumentFeature(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            showCircularProgress = showProgressBarOfPdfSavedAsImage,
                            viewModel = viewModel,
                            pathOfThePdfFile = pathOfThePdfFile,
                            saveWordFIleDialogBox = saveWordFIleDialogBox,
                            showBottomSheet = showBottomSheet,
                            nameOfPdfFileOutsideScope = nameOfThePdfFile,
                            uriOfFile = uriOfFile,
                            size = viewModel.listOfSize[index],
                            navHostController = navHostController,
                            showShareDialogBox = showShareDialogBox,
                            shareFIleAsPdf = shareFIleAsPdf,
                            shareFileAsImage = shareFileAsImages,
                            currentUri = currentUri,
                            showConvertingIntoImagesProgressDialogBox = showConvertingIntoImagesProgressDialogBox
                        )
                    }
                }
            }
        }
    }

    fun getPdfs(
        list: ArrayList<Uri>,
        activity: MainActivity,
        listOfPdfNames: ArrayList<String>,
        listOfDateAdded: ArrayList<String>
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
                    listOfDateAdded.add("$sizeInKBs KB")
                } else if (size.toDouble() / 1000000 > 1) {
                    val floatValue = size.toDouble() / 1000000
                    val sizeInMbs: Double = String.format("%.2f", floatValue).toDouble()
                    listOfDateAdded.add("$sizeInMbs MB")
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

    fun getFileDetail(uri: Uri, activity: MainActivity) {
        var cursor = activity.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            while (it.moveToFirst()) {
                var data = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            }
        }
    }
}

fun pullToRefresh() {

}
