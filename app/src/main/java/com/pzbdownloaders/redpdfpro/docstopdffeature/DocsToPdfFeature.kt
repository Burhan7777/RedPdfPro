package com.pzbdownloaders.redpdfpro.docstopdffeature

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.docstopdffeature.components.SingleRowDocxToPdf
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRowSplitFeature
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DocsToPdf(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    var listOfDocx = ArrayList<Uri>()

    val queryForSearch = remember { mutableStateOf("") }

    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            // var path = getFilePathFromContentUri(it!!, mainActivity)
            val python = Python.getInstance()
            val module = python.getModule("ocr")
            module.callAttr(
                "ocr",
                "storage/emulated/0/Pictures/ocr.jpg",
                "burhan123"
            )
        })

    LaunchedEffect(key1 = true) {
        getDocs(
            listOfDocx,
            activity,
            viewModel.listOfDocxNames,
            viewModel.listOfSize
        )
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfDocx = listOfDocx.toMutableStateList()
        }
    }

    val filteredPdfs = remember(queryForSearch.value, viewModel.mutableStateListOfDocx) {
        if (queryForSearch.value.isBlank()) {
            viewModel.mutableStateListOfDocx // Show all files if the query is empty
        } else {
            viewModel.mutableStateListOfDocx.filterIndexed { index, _ ->
                viewModel.listOfDocxNames[index].contains(queryForSearch.value, ignoreCase = true)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

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
                    val originalIndex = viewModel.mutableStateListOfDocx.indexOf(item)
                    if (originalIndex != -1) {
                        SingleRowDocxToPdf(
                            uri = item,
                            nameOfDocxFile = viewModel.listOfDocxNames[originalIndex],
                            activity = activity,
                            navHostController = navHostController,
                            viewModel = viewModel
                        )
                    } else {

                    }
                }
            }
        }
    }

}

fun getDocs(
    list: ArrayList<Uri>,
    activity: MainActivity,
    listOfDocNames: ArrayList<String>,
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
    val mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
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
            listOfDocNames.add(name)

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