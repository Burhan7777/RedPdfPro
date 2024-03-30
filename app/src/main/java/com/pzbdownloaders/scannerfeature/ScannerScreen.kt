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
import androidx.compose.runtime.MutableState
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
import com.pzbdownloaders.scannerfeature.components.SaveFIleAsPdf
import com.pzbdownloaders.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.scannerfeature.components.SavePdfAsImage
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
    var resultFromActivity: MutableState<GmsDocumentScanningResult?> = mutableStateOf(null)
    val showSaveDialogBox =
        mutableStateOf(false) //  When we return from the scanner activity this dialog shows to save the pdf
    val showProgressDialogBox =
        mutableStateOf(false) // When we press the save as image button this dialog box appears
    val showWordFIleSaveDialogBox =
        mutableStateOf(false) // When we press the save as docx button this dialog box appears can asks for name of save file
    val name =
        mutableStateOf("") // This is the name of the file which is to be saved as pdf when we return from scanner activity(Google's scanner activity)
    val nameOfWordFile =
        mutableStateOf("") // This is the name of docx file when we save pdf as docx file
    val pathOfPdfFile =
        mutableStateOf("") // Path of the word file. This is passed to the singleRow and it becomes equal to the path of the selected pdf. It is important since we save docx file in viewmodel so we need this path here in this screen.
    var message =
        mutableStateOf("Saving pdf as jpeg") // This is the message of progress dialog box when we save the pdf as images
    val messageSavingWordFIle =
        mutableStateOf("Saving pdf as docx") // This is the message of progress dialog box when we save the pdf as docx filer.

    //  Why isn't there equivalent for "showSaveDialogBox" for files converted in docx. Well the equivalent is "showProgressDialogBoxOfWordFile" and it comes from viewmodel. This is because this needs to be passed on to "DownloadPdfAsWord" file and that methods of that file are called in viewmodel
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
                resultFromActivity.value =
                    GmsDocumentScanningResult.fromActivityResultIntent(it.data)
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


            SaveFIleAsPdf(showSaveDialogBox, name, resultFromActivity, activity, viewModel)
            SavePdfAsImage(showProgressDialogBox = showProgressDialogBox, message = message)
            SavePdfAsDocxFile(
                showWordFIleSaveDialogBox,
                nameOfWordFile,
                viewModel,
                activity,
                pathOfPdfFile,
                messageSavingWordFIle
            )




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





