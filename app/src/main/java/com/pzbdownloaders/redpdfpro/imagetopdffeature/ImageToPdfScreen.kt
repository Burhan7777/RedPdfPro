package com.pzbdownloaders.redpdfpro.imagetopdffeature

import android.app.Activity
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import java.io.File
import java.io.FileOutputStream

@Composable
fun ImageToPdf(activity: MainActivity) {
    val showSaveDialogBox = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    var resultFromActivity: MutableState<GmsDocumentScanningResult?> =
        remember { mutableStateOf(null) }

    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
        .setResultFormats(
            GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
            GmsDocumentScannerOptions.RESULT_FORMAT_PDF
        )
        .setGalleryImportAllowed(true).build()

    val scanner = GmsDocumentScanning.getClient(options)

    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {

            if (it.resultCode == Activity.RESULT_OK) {
                resultFromActivity.value =
                    GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                showSaveDialogBox.value = true
            }
        }
    )


    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Column(modifier = Modifier.fillMaxSize()) {
        if (showSaveDialogBox.value) {
            AlertDialogBox(
                name = name,
                onDismiss = { showSaveDialogBox.value = !showSaveDialogBox.value }) {
                resultFromActivity.value?.pdf?.let { pdf ->
                    var externalDIr =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    var file = File("$externalDIr/Pro Scanner/Pdfs")
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    var path =
                        File("$externalDIr/Pro Scanner/Pdfs/${name.value}.pdf")
                    if (!path.exists()) {
                        path.createNewFile()
                    }
                    var fos = FileOutputStream(
                        path
                    )
                    activity.contentResolver.openInputStream(pdf.uri).use { inputStream ->
                        inputStream?.copyTo(fos)
                    }
                    Toast.makeText(activity, "File saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(start = 20.dp, end = 20.dp)
                .clickable {
                    scanner
                        .getStartScanIntent(activity)
                        .addOnSuccessListener {
                            result.launch(
                                IntentSenderRequest
                                    .Builder(it)
                                    .build()
                            )
                        }
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

}