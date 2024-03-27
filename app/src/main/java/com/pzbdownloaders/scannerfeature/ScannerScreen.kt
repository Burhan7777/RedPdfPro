package com.pzbdownloaders.scannerfeature

import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Environment
import android.speech.tts.TextToSpeech.EngineInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import org.jetbrains.annotations.Async
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

@Composable
fun ScannerScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setGalleryImportAllowed(true).build()

    val scanner = GmsDocumentScanning.getClient(options)

    var imageUris = mutableStateOf<List<Uri>>(emptyList())
    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {

            if (it.resultCode == RESULT_OK) {
                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                imageUris.value = result?.pages?.map { it.imageUri } ?: emptyList()
                result?.pdf?.let { pdf ->
                    var externalDIr =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    var file = File("$externalDIr/Pro Scanner")
                    if (!file.exists()) {
                        file.mkdirs()
                    }
                    var path =
                        File("$externalDIr/Pro Scanner/scanned${UUID.randomUUID()}.pdf")
                    if (!path.exists()) {
                        path.createNewFile()
                    }

                    var fos = FileOutputStream(
                        path
                    )
                    activity.contentResolver.openInputStream(pdf.uri).use { inputStream ->
                        inputStream?.copyTo(fos)
                    }
                }

            }
        }
    )
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scanner.getStartScanIntent(activity).addOnSuccessListener {
                    result.launch(IntentSenderRequest.Builder(it).build())
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "camera"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            var file = File("storage/emulated/0/Download/Pro Scanner")
            var files = file.listFiles()
        }
    }
}