package com.pzbdownloaders.redpdfpro.rotatepdffeature.screens

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.LoadingDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.scanFile
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.rotatepdffeature.components.RotateDialogBox
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRow
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.LazyColumnVer
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ViewPdfRotateScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController,
    path: String,
    uri: String
) {
    var pageNumbersSelected = remember { mutableStateOf(ArrayList<Int>()) }


    val scope = CoroutineScope(Dispatchers.Default)

    val name = remember { mutableStateOf("") }

    var showRadioDialogBox = remember { mutableStateOf(false) }

    var selectedRotateAngle = remember { mutableIntStateOf(0) }


    val showProgress = remember { mutableStateOf(false) }

    val context = LocalContext.current

    println("PATH:$path")

    var options = arrayOf("90", "180", "270")

    var showAlertBox = remember { mutableStateOf(false) }


    var pdfRenderer1: MutableState<PdfRenderer?> = remember { mutableStateOf(null) }

    lateinit var parcelFileDescriptor: ParcelFileDescriptor

    var showLazyColumn by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showProgress.value) {
                LoadingDialogBox("Pages are being rotated")
            }
            if (path != "") {
                var file = File(path)
                parcelFileDescriptor =
                    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer1.value = PdfRenderer(parcelFileDescriptor)
                var totalPagesPathFile = pdfRenderer1.value!!.pageCount
                println("PAGE COUNT:$totalPagesPathFile")
                LazyColumnVer1(
                    totalPages = totalPagesPathFile.toString().toInt(),
                    context = context,
                    file = file!!,
                    pdfRenderer1.value!!,
                    pageNumbersSelected.value,
                    false,
                    viewModel
                )
            }
        }
        Button(
            onClick = {
                if (pageNumbersSelected.value.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please select pages to be rotated",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    showRadioDialogBox.value = !showRadioDialogBox.value
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter),
            shape = MaterialTheme.shapes.medium.copy(
                all = CornerSize(10.dp)
            )
        ) {
            Text(text = stringResource(id = R.string.rotatePDF))
        }
        if (showAlertBox.value)
            AlertDialogBox(
                name = name,
                showRotateDialogBox = showRadioDialogBox,
                featureExecution = {
                    showProgress.value = true
                    scope.launch(Dispatchers.IO) {
                        val python = Python.getInstance()
                        val module = python.getModule("rotatePDF")
                        var result = module.callAttr(
                            "rotate_pdf",
                            path,
                            selectedRotateAngle.value,
                            pageNumbersSelected.value.toArray(),
                            name.value
                        )
                        withContext(Dispatchers.Main) {
                            if (result.toString() == "Success") {
                                withContext(Dispatchers.Main) {
                                    showProgress.value = false
                                }
                                Toast.makeText(
                                    context,
                                    "Filed saved",
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

                                        )
                                )
                            } else if (result.toString() == "Failure") {
                                showProgress.value = false
                                Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                },
                onDismiss = { showAlertBox.value = false })

        if (showRadioDialogBox.value) {
            RotateDialogBox(
                showAlertDialog = showAlertBox,
                selectedRotateAngle = selectedRotateAngle,
                options = options,
                onDismiss = { showRadioDialogBox.value = false }) {

            }
        }
    }
}

@Composable
fun LazyColumnVer1(
    totalPages: Int,
    context: Context,
    file: File,
    pdfRenderer: PdfRenderer,
    pageNoSelected: ArrayList<Int>,
    darkTheme: Boolean,
    viewModel: MyViewModel
) {


    var scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        for (i in 0 until totalPages) {
            var bitmap = loadPage(
                i,
                pdfRenderer,
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
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items = viewModel.modelList) { index, item ->
            SingleRow(model = item, pageNo = index, pageNoSelected)
        }

    }
}