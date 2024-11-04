package com.pzbdownloaders.redpdfpro.aifeature.main.screens

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.aifeature.textrecognitionfeature.presentation.ShowRecognizedText
import com.pzbdownloaders.redpdfpro.aifeature.textrecognitionfeature.presentation.TextRecognition
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel

@Composable
fun AIMainScreen(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    var resultFromActivity: MutableState<GmsDocumentScanningResult?> =
        remember { mutableStateOf(null) }
    var recognizeText = remember { mutableStateOf(false) }
    var showTextRecognitionDialogBox = remember { mutableStateOf(false) }

    var recognizedText = remember { mutableStateOf(StringBuilder()) }

    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setGalleryImportAllowed(true).build()

    val scanner = GmsDocumentScanning.getClient(options)

    // println(viewModel.modelScanner.size)
    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {

            if (it.resultCode == RESULT_OK) {
                resultFromActivity.value =
                    GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                recognizeText.value = true
            }

        }
    )


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
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
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.text_recognition),
                            contentDescription = stringResource(
                                id = R.string.text_recognition
                            ),
                            modifier = Modifier
                                .padding(top = 20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = stringResource(id = R.string.text_recognition),
                                fontSize = 25.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.text_recognition_summary),
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp),
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
            if (recognizeText.value) {
                TextRecognition(resultFromActivity, showTextRecognitionDialogBox, recognizedText)
            }
            if (showTextRecognitionDialogBox.value) {
                ShowRecognizedText(
                    mutableStateOf(recognizedText.value.toString()),
                    recognizedText
                ) {
                    showTextRecognitionDialogBox.value = false
                }
            }
        }
    }
}