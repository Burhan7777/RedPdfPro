package com.pzbdownloaders.redpdfpro.splitpdffeature.screens

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRow
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun SplitPdf(navHostController: NavHostController, activity: MainActivity, viewModel: MyViewModel) {

    val context = LocalContext.current
    var pageNumbersSelected = remember { mutableStateOf(ArrayList<Int>()) }
    var path by remember {
        mutableStateOf(" ")
    }

    var name = remember {
        mutableStateOf("")
    }
    var showAlertBox by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    var showProgress by remember {
        mutableStateOf(false)
    }

    val showPasswordAlertBox = remember {
        mutableStateOf(false)
    }
    val passwordOfLockedFile = remember {
        mutableStateOf("")
    }

    var totalPages by remember { mutableStateOf(0) }
    lateinit var pdfRenderer: PdfRenderer
    lateinit var parcelFileDescriptor: ParcelFileDescriptor
    var file by remember { mutableStateOf<File?>(null) }

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                path = getFilePathFromContentUri(it, activity)!!
                file = File(path)
                val python = Python.getInstance()
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
                }
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

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (totalPages == 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
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
                    Image(
                        painter = painterResource(id = R.drawable.upload),
                        contentDescription = stringResource(
                            id = R.string.upload
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.addPDF),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                }
            }
            Box(contentAlignment = Alignment.Center) {
                if (showProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp)
                            .zIndex(10f),
                        color = Color.Red
                    )
                }


                if (totalPages > 0 && file != null) {
                    parcelFileDescriptor =
                        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                    pdfRenderer = PdfRenderer(parcelFileDescriptor)
                    LazyColumnVer(
                        totalPages = totalPages.toString().toInt(),
                        context = context,
                        file = file!!,
                        pdfRenderer,
                        pageNumbersSelected.value,
                        viewModel
                    )
                }
            }
        }
        if (file != null) {
            Button(
                onClick = {
                    if (pageNumbersSelected.value.isEmpty()) {
                        Toast.makeText(
                            context,
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
        }
    }
    if (showAlertBox)
        AlertDialogBox(
            name = name,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("splitPDF")
                    var result = module.callAttr(
                        "split",
                        path,
                        pageNumbersSelected.value.toArray(),
                        name.value
                    )
                    withContext(Dispatchers.Main) {
                        if (result.toString() == "Success") {
                            withContext(Dispatchers.Main) {
                                showProgress = false
                            }
                            Toast.makeText(
                                context,
                                "Filed saved in /storage/emulated/0/Download/RedPdf/pdfs/${name}.pdf",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (result.toString() == "Failure") {
                            showProgress = false
                            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            onDismiss = { showAlertBox = false })

    if (showPasswordAlertBox.value) {
        AlertDialogBox(
            name = passwordOfLockedFile,
            id = R.string.enterPassword,
            onDismiss = { showPasswordAlertBox.value = false }) {

        }
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


    var scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        for (i in 0 until totalPages) {
            var bitmap = loadPage(
                i,
                pdfRenderer
            )
            withContext(Dispatchers.Main) {
                if (viewModel.modelList.size < totalPages)
                    viewModel.modelList.add(modelBitmap(bitmap, isSelected = mutableStateOf(false)))
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


