package com.pzbdownloaders.redpdfpro.unlockpdffeature

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UnlockPdf(activity: MainActivity) {

    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    var showProgress by remember {
        mutableStateOf(false)
    }

    val showAlertBox = remember { mutableStateOf(false) }

    val showPasswordDialogBox = remember { mutableStateOf(false) }

    val password = remember { mutableStateOf("") }

    val name = remember { mutableStateOf("") }

    val path = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                path.value = getFilePathFromContentUri(it, activity)!!
                showPasswordDialogBox.value = !showPasswordDialogBox.value
            }
        })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
    }
    if (showPasswordDialogBox.value)
        AlertDialogBox(
            name = password,
            id = R.string.existingPassword,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showAlertBox.value = !showAlertBox.value
                }
            },
            onDismiss = { showPasswordDialogBox.value = false })

    if (showAlertBox.value) {
        AlertDialogBox(
            name = name,
            featureExecution = {
                scope.launch(Dispatchers.IO) {
                    showProgress = true
                    val python = Python.getInstance()
                    val module = python.getModule("unlockPDF")
                    var result = module.callAttr(
                        "unlock_pdf",
                        path.value,
                        password.value,
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
            onDismiss = { showAlertBox.value = false })
    }
}