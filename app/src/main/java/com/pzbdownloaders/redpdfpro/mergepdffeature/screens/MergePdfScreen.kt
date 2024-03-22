package com.pzbdownloaders.redpdfpro.mergepdffeature.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun MergePdf(activity: MainActivity, viewModel: MyViewModel) {

    var showAlertBox = remember {
        mutableStateOf(false)
    }

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                viewModel.pdfNames.add(getFileName(it!!, activity))
                viewModel.listOfPdfToMerge.add(getFilePathFromContentUri(it, activity = activity)!!)

            }
        })


    var name = remember {
        mutableStateOf("")
    }

    var showProgress by remember {
        mutableStateOf(false)
    }

    var context = LocalContext.current
    var scope = rememberCoroutineScope()

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(10.dp)

            ) {
                items(count = viewModel.pdfNames.size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                width = 1.dp,
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " ${viewModel.pdfNames[it]}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 20.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                    }
                }
            }
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

        if (showProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .zIndex(10f)
                    .align(Alignment.Center),
                color = Color.Red
            )
        }
        if (viewModel.listOfPdfToMerge.size > 1) {
            Button(
                onClick = { showAlertBox.value = !showAlertBox.value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(id = R.string.mergePDFs))
            }
        }
        if (showAlertBox.value) {
            AlertDialogBox(
                name = name,
                listOfPdfs = viewModel.listOfPdfToMerge,
                onDismiss = { showAlertBox.value = !showAlertBox.value }) {
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("mergePDF")
                    val result = module.callAttr(
                        "merge_pdf",
                        viewModel.listOfPdfToMerge.toTypedArray(),
                        name.value
                    )
                    withContext(Dispatchers.Main) {
                        if (result.toString() == "Success") {
                            showProgress = false
                            Toast.makeText(
                                context,
                                "Successfully saved at /storage/emulated/0/Download/${name}.pdf",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (result.toString() == "Failure") {
                            showProgress = false
                            Toast.makeText(context, "Operation Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}