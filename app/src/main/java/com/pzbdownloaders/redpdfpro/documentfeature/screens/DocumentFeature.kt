package com.pzbdownloaders.redpdfpro.documentfeature.screens

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.documentfeature.components.SingleRowDocumentFeature
import com.pzbdownloaders.redpdfpro.scannerfeature.components.BottomSheet.BottomSheet
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar


@Composable
fun DocumentFeature(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    var listOfPdfs = ArrayList<Uri>()
    var scope = rememberCoroutineScope()
    val showProgressBarOfPdfSavedAsImage = mutableStateOf(false)
    val nameOfTheWordFile = mutableStateOf("")
    val saveWordFIleDialogBox = mutableStateOf(false)
    val pathOfThePdfFile = mutableStateOf("")
    val showBottomSheet = mutableStateOf(false)
    val showDeleteDialogBox = mutableStateOf(false)
    val showPasswordDialogBox = mutableStateOf(false)
    val showSaveDialogBox = mutableStateOf(false)
    val nameOfThePdfFile = mutableStateOf<String?>("")
    val uriOfFile = mutableStateOf<Uri>(Uri.EMPTY)
    val showShareDialogBox = remember { mutableStateOf(false) }
    val shareFIleAsPdf = remember { mutableStateOf(false) }
    val shareFileAsImages = remember { mutableStateOf(false) }
    var currentUri = remember { mutableStateOf<Uri?>(null) }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = true) {
        if (viewModel.mutableStateListOfPdfs.size == 0 && viewModel.listOfPdfNames.size == 0) {
            getPdfs(listOfPdfs, activity, viewModel.listOfPdfNames, viewModel.listOfSize)
            withContext(Dispatchers.Main) {
                viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
            }
        }
    }


    //  println(viewModel.mutableStateListOfPdfs.size)
//    println(viewModel.listOfPdfNames.size)

    viewModel.modelList.clear()

    SavePdfAsImage(
        showProgressDialogBox = showProgressBarOfPdfSavedAsImage,
        message = mutableStateOf("Save pdf as image")
    )
    SavePdfAsDocxFile(
        showWordFIleSaveDialogBox = saveWordFIleDialogBox,
        nameOfWordFile = nameOfTheWordFile,
        viewModel = viewModel,
        activity = activity,
        pathOfPdfFile = pathOfThePdfFile,
        messageSavingWordFIle = mutableStateOf("Saving pdf as .docx"),
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


    Column(modifier = Modifier.fillMaxSize()) {
        if (showShareDialogBox.value) {
            AlertDialog(onDismissRequest = { showShareDialogBox.value = false },
                confirmButton = {},
                title = {
                    Column {
                        Text(text = stringResource(id = R.string.shareAsPdf), Modifier.clickable {
                            shareFIleAsPdf.value = true
                        })
                        Text(
                            text = stringResource(id = R.string.shareAsImages),
                            Modifier.clickable {
                                shareFileAsImages.value = true
                            })
                    }
                })
        }
        LazyColumn(state = lazyListState) {
            itemsIndexed(items = viewModel.mutableStateListOfPdfs) { index, item ->
                SingleRowDocumentFeature(
                    uri = item,
                    nameOfPdfFile = viewModel.listOfPdfNames[index],
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
                    currentUri = currentUri
                )
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
