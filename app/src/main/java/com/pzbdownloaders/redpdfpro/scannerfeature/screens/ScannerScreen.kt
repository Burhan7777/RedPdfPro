package com.pzbdownloaders.redpdfpro.scannerfeature.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.ProgressDialogBox
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.documentfeature.util.ShareAsPdfOrImage
import com.pzbdownloaders.redpdfpro.scannerfeature.components.BottomSheet.BottomSheet
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SaveFIleAsPdf
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsImage
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsText
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SingleRowScannerMainScreen
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {

    var path: File? = null
    var resultFromActivity: MutableState<GmsDocumentScanningResult?> = remember{mutableStateOf(null)}
    val showSaveDialogBox =
        remember{mutableStateOf(false)} //  When we return from the scanner activity this dialog shows to save the pdf
    val showProgressDialogBox =
        remember{mutableStateOf(false)} // When we press the save as image button this dialog box appears
    val showWordFIleSaveDialogBox =
        remember{mutableStateOf(false)} // When we press the save as docx button this dialog box appears can asks for name of save file
    val showTextFileSaveDialogBox =
        remember{mutableStateOf(false)} // // When we press the save as text button this dialog box appears can asks for name of save file
    val showRenameSaveDialogBox = remember{mutableStateOf(false)}
    val showDeleteDialogBox = remember{mutableStateOf(false)}
    val showPasswordDialogBox =
        remember{mutableStateOf(false)}//  When we press "lock PDF" in bottom sheet this is first dialog box which appears asking for a password

    val showSaveAsLockPdfBox =
        remember{mutableStateOf(false)} // When we press "lock PDF" in bottom sheet this is the second dialog box which appears asking for name of the file
    val rename = remember{mutableStateOf("")}
    val name =
        remember{ mutableStateOf("")} // This is the name of the file which is to be saved as pdf when we return from scanner activity(Google's scanner activity)
    val nameOfWordFile =
        remember{mutableStateOf("")} // This is the name of docx file when we save pdf as docx file
    val nameOfTextFile =
        remember{mutableStateOf("")}// This is the name of txt file when we save pdf as txt file
    val pathOfPdfFile =
        remember{mutableStateOf("")} // Path of the word file. This is passed to the singleRow and it becomes equal to the path of the selected pdf. It is important since we save docx file in viewmodel so we need this path here in this screen.
    val bitmapOfPdfFile = remember{mutableStateOf<Bitmap?>(null)}
    val nameOfPdfFIle =remember{ mutableStateOf<String?>("")}
    var message =
        remember{mutableStateOf("Saving pdf as jpeg")} // This is the message of progress dialog box when we save the pdf as images
    val messageSavingWordFIle =
        remember{mutableStateOf("Saving pdf as docx")} // This is the message of progress dialog box when we save the pdf as docx filer.
    val messageSavingTextFile =
        remember{mutableStateOf("Saving pdf as txt")} // This is the message of progress dialog box when we save the pdf as txt filer.
    val showBottomSheet = remember{mutableStateOf(false)}
    val showShareDialogBox =
        remember{mutableStateOf(false)} // This shows the share as pdf or image dialog box
    val shareFileAsPdf = remember{mutableStateOf(false)} // Shares the file as pdf
    val shareFileAsImage = remember{mutableStateOf(false)} // Shares the file as images
    val rememberFilePathSoThatItCanBeShared =
        remember { mutableStateOf("") } // remembers the path of the file because due to recomposition scannermodel.path resets to top  pdf
    val showConvertingIntoImagesProgressDialogBox = remember{mutableStateOf(false)}
    val queryForSearch = remember{mutableStateOf("")}
    val searchActiveBoolean = remember{mutableStateOf(false)}
    //  Why isn't there equivalent for "showSaveDialogBox" for files converted in docx. Well the equivalent is "showProgressDialogBoxOfWordFile" and it comes from viewmodel. This is because this needs to be passed on to "DownloadPdfAsWord" file and that methods of that file are called in viewmodel
    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setGalleryImportAllowed(true).build()




    viewModel.modelList.clear()
    //viewModel.listOfFiles.clear()
    //  viewModel.modelScanner.clear()
    // viewModel.pdfNames.clear()
    //viewModel.listOfPdfToMerge.clear()
    println(viewModel.modelScanner.size)

    if (ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        /*  val file = File("$externalDir/Pro Scanner/Pdfs")
          if (!file.exists()) {
              file.mkdirs()
          }*/


    }
    var externalDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File("$externalDir/Pro Scanner/Scanned Pdfs")
    if (!file.exists()) {
        file.mkdirs()
    }

    val filteredPdfs = remember(queryForSearch.value, viewModel.listOfFiles) {
        if (queryForSearch.value.isBlank()) {
            viewModel.listOfFiles // Show all files if the query is empty
        } else {
            viewModel.listOfFiles.filterIndexed { index, _ ->
                viewModel.modelScanner[index].name?.contains(queryForSearch.value, ignoreCase = true)!!
            }
        }
    }

    // println(file.listFiles().size)
    //println(file.absolutePath)
    if (Build.VERSION.SDK_INT >= 30) {
        if (Environment.isExternalStorageManager()) {
            if (viewModel.listOfFiles.size < (file.listFiles()?.size ?: 0)) {
                viewModel.listOfFiles =
                    file.listFiles()?.toCollection(ArrayList()) ?: ArrayList<File>()
                viewModel.listOfFiles.reverse()
                viewModel.getImage()
            }
        }
    } else {
        if (viewModel.listOfFiles.size < (file.listFiles()?.size ?: 0)) {
            viewModel.listOfFiles =
                file.listFiles()?.toCollection(ArrayList()) ?: ArrayList<File>()
            viewModel.listOfFiles.reverse()
            viewModel.getImage()
        }
    }

    val scanner = GmsDocumentScanning.getClient(options)

    // println(viewModel.modelScanner.size)
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
    val context = LocalContext.current


// launch the above intent.

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scanner.getStartScanIntent(activity).addOnSuccessListener {
                        result.launch(IntentSenderRequest.Builder(it).build())
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Scan pages",
                    tint = MaterialTheme.colorScheme.onSecondary
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

            androidx.compose.material.OutlinedTextField(
                value = queryForSearch.value,
                onValueChange = { queryForSearch.value = it },
                label = { Text("Search PDFs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(10.dp),
                    topEnd = CornerSize(10.dp),
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp),
                )
            )

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
            SavePdfAsText(
                nameOfTextFile = nameOfTextFile,
                showTextFileSaveDialogBox = showTextFileSaveDialogBox,
                viewModel = viewModel,
                activity = activity,
                pathOfPdfFile = pathOfPdfFile,
                messageSavingTextFIle = messageSavingTextFile
            )
            BottomSheet(
                showBottomSheet = showBottomSheet,
                showDeleteDialogBox = showDeleteDialogBox,
                viewModel = viewModel,
                activity = activity,
                navHostController = navHostController,
                pathOfPdfFile = pathOfPdfFile,
                nameOfPdfFIle = nameOfPdfFIle,
                bitmapOfPdfFile = bitmapOfPdfFile,
                showRenameSaveDialogBox = showRenameSaveDialogBox,
                rename = rename,
                showPasswordDialogBox = showPasswordDialogBox,
                showSaveAsLockPdfBox = showSaveAsLockPdfBox
            )

            if (showShareDialogBox.value) {
                ShareAsPdfOrImage(
                    shareFIleAsPdf = shareFileAsPdf,
                    shareFileAsImages = shareFileAsImage,
                    showShareDialogBox = showShareDialogBox
                )
            }

            if (showConvertingIntoImagesProgressDialogBox.value) {
                ProgressDialogBox(message = mutableStateOf(stringResource(id = R.string.convertingIntoImages)))
            }


            LazyColumn(
                contentPadding = PaddingValues(bottom = 70.dp)
            ) {
                items(
                    items = viewModel.modelScanner
                ) { scannerModel ->
                    SingleRowScannerMainScreen(
                        scannerModel,
                        showProgressDialogBox,
                        nameOfWordFile,
                        pathOfPdfFile,
                        showWordFIleSaveDialogBox,
                        showTextFileSaveDialogBox,
                        activity,
                        showBottomSheet,
                        bitmapOfPdfFile,
                        nameOfPdfFIle,
                        showShareDialogBox,
                        shareFileAsPdf,
                        shareFileAsImage,
                        rememberFilePathSoThatItCanBeShared,
                        showConvertingIntoImagesProgressDialogBox,
                        navHostController
                    )
                }
            }
        }
    }
}




