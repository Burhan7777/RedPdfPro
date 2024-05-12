package com.pzbdownloaders.redpdfpro.pdfreaderfeature.screens

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PdfReader(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController,
    uri: String,
    file: String
) {
    val listOfBitmaps = mutableStateListOf<Bitmap>()


    if (file == "") {
        PdfRendererViewCompose(
            uri = Uri.parse(uri),
            lifecycleOwner = LocalLifecycleOwner.current
        )
    } else {
        PdfRendererViewCompose(
            file = File(file),
            lifecycleOwner = LocalLifecycleOwner.current
        )
    }

    /*   var showCircularProgressIndicator = mutableStateOf(true)
       if (showCircularProgressIndicator.value) {
           CircularProgressIndicator()
       }
       val pdfState = rememberVerticalPdfReaderState(
           resource = ResourceType.Local(
               Uri.parse(uri)
           ),
           isZoomEnable = true
       )

       showCircularProgressIndicator.value = false
       VerticalPDFReader(
           state = pdfState,
           modifier = Modifier
               .fillMaxSize()
               .background(color = Color.Gray)
       )
       LinearProgressIndicator(
           progress = pdfState.loadPercent / 100f,
           modifier = Modifier.fillMaxWidth(),
           color = Color.Red,
           strokeCap = StrokeCap.Square,
           trackColor = Color.Green
       )*/


    /*    val scope = rememberCoroutineScope()
        scope.launch(Dispatchers.Default) {
            val path = getFilePathFromContentUri(Uri.parse(uri), activity)
            val file = File(path!!)
            var parcelFileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            for (i in 0 until pdfRenderer.pageCount) {
                val bitmap = loadPage(i, pdfRenderer)
                listOfBitmaps.add(bitmap)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = listOfBitmaps.size) {
                    Image(
                        bitmap = listOfBitmaps[it].asImageBitmap(),
                        contentDescription = "Pdf bitmap",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }*/


}