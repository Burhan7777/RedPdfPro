package com.pzbdownloaders.redpdfpro.mergepdffeature.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import java.io.File

@Composable
fun MergePdf(activity: MainActivity, viewModel: MyViewModel) {
    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            viewModel.pdfNames.add(getFileName(it!!, activity))
            viewModel.listOfPdfToMerge.add(getFilePathFromContentUri(it, activity = activity)!!)
        })

    var showAlertBox = remember {
        mutableStateOf(false)
    }

    var name = remember {
        mutableStateOf("")
    }

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
            Button(
                onClick = { result.launch("application/pdf") },
                modifier = Modifier.padding(top = 30.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.addPDF),

                    )
            }
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
                val scope = CoroutineScope(Dispatchers.IO)
                val python = Python.getInstance()
                val module = python.getModule("mergePDF")
                scope.launch(
                    Dispatchers.IO
                ) {
                    module.callAttr(
                        "merge_pdf",
                        viewModel.listOfPdfToMerge.toTypedArray(),
                        name.value
                    )
                }
            }
        }
    }

}