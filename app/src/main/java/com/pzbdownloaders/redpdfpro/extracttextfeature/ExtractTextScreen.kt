package com.pzbdownloaders.redpdfpro.extracttextfeature

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri

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
    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                nameOfFile.value = getFileName(it, mainActivity)
                path = getFilePathFromContentUri(it, mainActivity)!!
            }

        })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        ) {
            Button(
                onClick = { result.launch("application/pdf") }) {
                Text(text = stringResource(id = R.string.addPDF))
            }
        }
        Button(
            onClick = {
                alertDialogBox = !alertDialogBox

            }, modifier = Modifier.align(
                Alignment.BottomCenter
            )
        ) {
            Text(text = stringResource(id = R.string.extractText))
        }
        if (alertDialogBox) {
            AlertDialogBox(name = name, onDismiss = { alertDialogBox = !alertDialogBox }) {
                val python = Python.getInstance()
                val module = python.getModule("extractTextPDF")
                module.callAttr("extract_text_pypdf", path, name.value)
            }
        }
    }

}