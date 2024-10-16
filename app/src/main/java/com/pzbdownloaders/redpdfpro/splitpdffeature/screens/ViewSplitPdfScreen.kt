package com.pzbdownloaders.redpdfpro.splitpdffeature.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.scanFile
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRow
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ViewSplitPdfScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController,
    path: String,
    uri: String
) {

    var pageNumbersSelected = remember { mutableStateOf(ArrayList<Int>()) }

    var showAlertBox by remember { mutableStateOf(false) }

    val scope = CoroutineScope(Dispatchers.Default)

    val name = remember { mutableStateOf("") }

    var totalPagesPathFile = 0

    var showUnlockDialogBox = remember { mutableStateOf(false) }

    var password = remember { mutableStateOf("") }

    val showProgress = remember { mutableStateOf(false) }

    var pdfRenderer1: MutableState<PdfRenderer?> = remember { mutableStateOf(null) }

    var showLazyColumn by remember { mutableStateOf(false) }
    if (path != "") {
        val file = File(path)
        var parcelFileDescriptor =
            ParcelFileDescriptor.open(
                file,
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        try {
            pdfRenderer1.value = PdfRenderer(parcelFileDescriptor)
            totalPagesPathFile = pdfRenderer1.value!!.pageCount
        } catch (exception: SecurityException) {
        }


        if (totalPagesPathFile > 0) {
            showLazyColumn = true
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if (pdfRenderer1.value != null) {
                LazyColumnVer(
                    totalPages = totalPagesPathFile,
                    context = activity,
                    file = file,
                    pdfRenderer = pdfRenderer1.value!!,
                    pageNoSelected = pageNumbersSelected.value,
                    viewModel = viewModel,
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "This file is locked",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showUnlockDialogBox.value = true
                        },
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Unlock", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
            if (showProgress.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .zIndex(10f)
                        .align(Alignment.Center),
                    color = Color.Red
                )
            }
            Button(
                onClick = {
                    if (pageNumbersSelected.value.isEmpty()) {
                        Toast.makeText(
                            activity,
                            "Please select pages to be split",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else
                        showAlertBox = !showAlertBox
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                shape = MaterialTheme.shapes.medium.copy(
                    all = CornerSize(10.dp)
                )
            ) {
                Text(text = stringResource(id = R.string.splitPdf))
            }
            if (showAlertBox)
                AlertDialogBox(
                    name = name,
                    featureExecution = {
                        scope.launch(Dispatchers.IO) {
                            lateinit var result: PyObject
                            showProgress.value = true
                            val python = Python.getInstance()
                            val module = python.getModule("splitPDF")
                            result = module.callAttr(
                                "split",
                                path,
                                pageNumbersSelected.value.toArray(),
                                name.value
                            )
                            withContext(Dispatchers.Main) {
                                if (result.toString() == "Success") {
                                    withContext(Dispatchers.Main) {
                                        showProgress.value = false
                                    }
                                    val externalDir =
                                        "${
                                            Environment.getExternalStoragePublicDirectory(
                                                Environment.DIRECTORY_DOWNLOADS
                                            )
                                        }/Pro Scanner/Pdfs"
                                    viewModel.listOfFiles.add(File("$externalDir/${name.value}.pdf"))
                                    viewModel.addItem()
                                    /*  var contentUri = FileProvider.getUriForFile(
                                          activity,
                                          activity.packageName + ".provider",
                                          File("$externalDir/${name.value}.pdf")
                                      )
                                      activity.contentResolver.notifyChange(contentUri, null)*/
                                    scanFile("$externalDir/${name.value}.pdf", activity)
                                    navHostController.navigate(
                                        Screens.FinalScreenOfPdfOperations.finalScreen(
                                            "$externalDir/${name.value}.pdf",
                                            "$externalDir/${name.value}.pdf",

                                            )
                                    )
                                } else if (result.toString() == "Failure") {
                                    showProgress.value = false
                                    Toast.makeText(
                                        activity,
                                        "Operation Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    },
                    onDismiss = { showAlertBox = false })
        }
    } else {
        Toast.makeText(
            activity,
            stringResource(id = R.string.failedToGetTheFIle),
            Toast.LENGTH_SHORT
        ).show()
    }
    if (showUnlockDialogBox.value) {
        AlertDialogBox(name = password, id = R.string.enterPassword,
            featureExecution = {

            },
            onDismiss = {
                showUnlockDialogBox.value = false
            })
    }
}

@Composable
fun LazyColumnVer(
    totalPages: Int,
    context: Context,
    file: File,
    pdfRenderer: PdfRenderer,
    pageNoSelected: ArrayList<Int>,
    viewModel: MyViewModel
) {


    LaunchedEffect(true) {
        for (i in 0 until totalPages) {
            var bitmap = loadPage(
                i,
                pdfRenderer
            )
            withContext(Dispatchers.Main) {
                if (viewModel.modelList.size < totalPages)
                    viewModel.modelList.add(
                        modelBitmap(
                            bitmap,
                            isSelected = mutableStateOf(false)
                        )
                    )
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items = viewModel.modelList) { index, item ->
            SingleRow(model = item, pageNo = index, pageNoSelected)
        }
        item {
            Spacer(modifier = Modifier.height(250.dp))
        }
    }
}

