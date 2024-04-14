package com.pzbdownloaders.redpdfpro.documentfeature.screens

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.documentfeature.components.SingleRowDocumentFeature
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsImage
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

    scope.launch(Dispatchers.IO) {
        getPdfs(listOfPdfs, activity, viewModel.listOfPdfNames)
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()

        }
    }

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
        messageSavingWordFIle = mutableStateOf("Saving pdf as .docx")
    )


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn() {
            itemsIndexed(items = viewModel.mutableStateListOfPdfs) { index, item ->
                SingleRowDocumentFeature(
                    uri = item,
                    nameOfPdfFile = viewModel.listOfPdfNames[index],
                    activity = activity,
                    showCircularProgress = showProgressBarOfPdfSavedAsImage,
                    viewModel = viewModel,
                    pathOfThePdfFile = pathOfThePdfFile,
                    saveWordFIleDialogBox = saveWordFIleDialogBox
                )
            }
        }
    }
}

fun getPdfs(
    list: ArrayList<Uri>,
    activity: MainActivity,
    listOfPdfNames: ArrayList<String>
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
    val orderBy = MediaStore.Files.FileColumns.SIZE + " DESC"

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
