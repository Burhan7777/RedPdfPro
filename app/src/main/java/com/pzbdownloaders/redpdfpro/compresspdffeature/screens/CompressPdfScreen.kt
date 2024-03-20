package com.pzbdownloaders.redpdfpro.compresspdffeature.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri

@Composable
fun CompressPDF(mainActivity: MainActivity) {
    var nameOfFile by remember { mutableStateOf("") }
    var path by remember { mutableStateOf("") }
    var name = mutableStateOf("")
    var alertDialogBox by remember { mutableStateOf(false) }
    var result = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult =
        {
            if (it != null) {
                nameOfFile = getFileName(it, mainActivity)
                path = getFilePathFromContentUri(it, mainActivity)!!
            }
        })
    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
        ) {
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
                    text = nameOfFile,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp),
                    overflow = TextOverflow.Ellipsis
                )

            }
            Button(onClick = { result.launch("application/pdf") }) {
                Text(text = stringResource(id = R.string.addPDF))
            }
        }

        Button(
            onClick = { alertDialogBox = !alertDialogBox },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = stringResource(id = R.string.compressPDF))
        }
    }
    if (alertDialogBox) {
        AlertDialogBox(name = name, onDismiss = { alertDialogBox = !alertDialogBox }) {
            val python = Python.getInstance()
            val module = python.getModule("compressPDF")
            module.callAttr("compress_pdf", path, name.value)
        }
    }

}