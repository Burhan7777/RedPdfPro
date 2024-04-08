package com.pzbdownloaders.redpdfpro.scannerfeature.components.BottomSheet

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BottomSheetLockItem(
    painter: Painter,
    contentDescriptionId: Int,
    nameId: Int,
    showPasswordDialogBox: MutableState<Boolean>,
    showSaveAsLockPdfBox: MutableState<Boolean>,
    pathOfPdfFile: MutableState<String>
) {
    val password = remember { mutableStateOf("") }
    val nameOfFile = remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    if (showPasswordDialogBox.value) {
        AlertDialogBox(
            name = password,
            id = R.string.setPassword,
            onDismiss = { showPasswordDialogBox.value = false }) {
            showPasswordDialogBox.value = false
            showSaveAsLockPdfBox.value = true
        }
    }
    if (showSaveAsLockPdfBox.value) {
        AlertDialogBox(
            name = nameOfFile,
            onDismiss = { showSaveAsLockPdfBox.value = false }) {
            showSaveAsLockPdfBox.value = false
            println(password.value)
            scope.launch(Dispatchers.Default) {
                println("hello")
                val python = Python.getInstance()
                val module = python.getModule("lockPDF")
                val response =
                    module.callAttr(
                        "lock_pdf",
                        pathOfPdfFile.value,
                        password.value,
                        nameOfFile.value
                    )
                if (response.toString() == "Success") {
                    withContext(Dispatchers.Main) {
                        android.widget.Toast.makeText(
                            context,
                            "File saved",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable {
                showPasswordDialogBox.value = true
            }
            .padding(top = 7.dp, bottom = 7.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = stringResource(id = contentDescriptionId),
                modifier = Modifier.padding(
                    start = 30.dp, top = 10.dp
                )
            )
            Text(
                text = stringResource(id = nameId),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}