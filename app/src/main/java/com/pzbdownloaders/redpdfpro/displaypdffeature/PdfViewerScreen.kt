package com.pzbdownloaders.redpdfpro.displaypdffeature

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage

@Composable
fun PdfViewer(
    uri: String,
    file: String
) {

    val context = LocalContext.current
    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context, uri, file)
    }

    val renderedPages = remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        renderedPages.value = pdfBitmapConverter.pdfToBitmaps(Uri.parse(uri))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(renderedPages.value) { page ->
                AsyncImage(
                    model = page,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(page.width.toFloat() / page.height.toFloat())
                )
            }
        }
    }
}