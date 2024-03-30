package com.pzbdownloaders.scannerfeature

import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.AlertDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.scannerfeature.components.SingleRowScannerMainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Arrays
import java.util.UUID


@Composable
fun ScannerScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {

    var path: File? = null
    var resultFromActivity: GmsDocumentScanningResult? = null
    val showSaveDialogBox = mutableStateOf(false)
    val showProgressDialogBox = mutableStateOf(false)
    val showWordFIleSaveDialogBox = mutableStateOf(false)
    val name = mutableStateOf("")
    val nameOfWordFile = mutableStateOf("")
    val pathOfPdfFile = mutableStateOf("")
    var message = mutableStateOf("Saving pdf as jpeg")
    val messageSavingWordFIle = mutableStateOf("Saving pdf as docx")

    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setGalleryImportAllowed(true).build()

    val scanner = GmsDocumentScanning.getClient(options)

    val file = File("storage/emulated/0/Download/Pro Scanner/Pdfs")
    if (viewModel.listOfFiles.size < (file.listFiles()?.size ?: 0)) {

        viewModel.listOfFiles =
            file.listFiles()?.toCollection(ArrayList()) ?: ArrayList<File>()
        viewModel.listOfFiles.reverse()
        viewModel.getImage()
    }

    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {

            if (it.resultCode == RESULT_OK) {
                resultFromActivity = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                showSaveDialogBox.value = true
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
                    contentDescription = "Scan pages"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.secondary),
        ) {

            if (showProgressDialogBox.value) {
                ProgressDialogBox(message = message)
            }


            if (showWordFIleSaveDialogBox.value) {
                AlertDialogBox(
                    name = nameOfWordFile,
                    onDismiss = {
                        showWordFIleSaveDialogBox.value = !showWordFIleSaveDialogBox.value
                    }) {
                    viewModel.showProgressDialogBoxOfWordFile.value = true
                    viewModel.convertPdfIntoAWordFIle(
                        activity.applicationContext,
                        pathOfPdfFile.value,
                        nameOfWordFile
                    )
                }
            }
            if (viewModel.showProgressDialogBoxOfWordFile.value) {
                ProgressDialogBox(message = messageSavingWordFIle)
            }
            if (showSaveDialogBox.value) {
                AlertDialogBox(
                    name = name,
                    onDismiss = { showSaveDialogBox.value = !showSaveDialogBox.value }) {
                    resultFromActivity?.pdf?.let { pdf ->
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
                        viewModel.listOfFiles.add(path!!)
                        viewModel.addItem()
                    }
                }
            }

            LazyColumn(
            ) {
                items(
                    items = viewModel.modelScanner.toList()
                ) { scannerModel ->
                    SingleRowScannerMainScreen(
                        scannerModel,
                        showProgressDialogBox,
                        nameOfWordFile,
                        pathOfPdfFile,
                        showWordFIleSaveDialogBox,
                        viewModel.showProgressDialogBoxOfWordFile
                    )
                }
            }
        }
    }
}
