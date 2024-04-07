package com.pzbdownloaders.redpdfpro.extracttextfeature

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ExtractText(mainActivity: MainActivity) {
    var path by remember { mutableStateOf("") }
    var nameOfFile = mutableStateOf("")
    var name = remember {
        mutableStateOf("")
    }
    var alertDialogBox by remember {
        mutableStateOf(false)
    }

    var showProgress by remember {
        mutableStateOf(false)
    }

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )


    var context = LocalContext.current

    var scope = rememberCoroutineScope()

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                nameOfFile.value = getFileName(it, mainActivity)
                path = getFilePathFromContentUri(it, mainActivity)!!
                alertDialogBox = !alertDialogBox
            }

        })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(
                Alignment.Center
            )
        ) {
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
        if (alertDialogBox) {
            AlertDialogBox(name = name, onDismiss = { alertDialogBox = !alertDialogBox }) {
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("extractTextPDF")
                    var result = module.callAttr("extract_text_pypdf", path, name.value)
                    withContext(Dispatchers.Main) {
                        showProgress = false
                        if (result.toString() == "Success") {
                            Toast.makeText(context, "File successfully saved", Toast.LENGTH_SHORT)
                                .show()
                        } else if (result.toString() == "Failure") {
                            showProgress = false
                            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }
        }
    }

}