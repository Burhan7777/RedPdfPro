package com.pzbdownloaders.redpdfpro.aifeature.textrecognitionfeature.presentation

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun TextRecognition(
    result: MutableState<GmsDocumentScanningResult?>,
    showTextRecognitionDialog: MutableState<Boolean>,
    recognizedText: MutableState<StringBuilder>
) {

    val context = LocalContext.current
    LaunchedEffect(true) {
        recognizedText.value.clear()
    }
    var listOfImageUris = remember { mutableStateListOf<Uri?>() }
    LaunchedEffect(result.value) {
        listOfImageUris.clear()
        result.value?.pages?.forEach { page ->
            listOfImageUris.add(page.imageUri)
        }
        recognizedText.value.clear() // Clear previous text on each new scan
    }

    println(listOfImageUris.size)

    LaunchedEffect(result.value) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        for (uri in listOfImageUris) {
            uri?.let {
                val image = InputImage.fromFilePath(context, it)

                // Suspend and await recognition result
                val textResult = withContext(Dispatchers.IO) {
                    runCatching { recognizer.process(image).await() }
                        .getOrNull() // Get text or null if failed
                }

                // If text is retrieved, append to recognizedText
                textResult?.textBlocks?.forEach { block ->
                    recognizedText.value.append("${block.text}\n")
                }
            }
        }

        // Show dialog only after all text blocks have been processed
        if (recognizedText.value.isNotEmpty()) {
            showTextRecognitionDialog.value = true
            println("VALUE: ${recognizedText.value}")
        } else {
            Toast.makeText(context, "Failed to recognize text", Toast.LENGTH_SHORT).show()
        }
    }
}





