package com.pzbdownloaders.redpdfpro.displaypdffeature

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs

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

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var lastPanTime by remember { mutableStateOf(0L) }
    val scrollThreshold = 50 // Adjust this value to change scroll sensitivity

    LaunchedEffect(key1 = true) {
        renderedPages.value = pdfBitmapConverter.pdfToBitmaps(Uri.parse(uri))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(renderedPages.value) { page ->
                var scale by remember { mutableStateOf(1f) }
                var offsetX by remember { mutableStateOf(0f) }
                var offsetY by remember { mutableStateOf(0f) }
                var previousOffset by remember { mutableStateOf(Offset.Zero) }
                var scrollJob by remember { mutableStateOf<Job?>(null) }

                AsyncImage(
                    model = page,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(page.width.toFloat() / page.height.toFloat())
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        }
                        .pointerInput(scale) {
                            detectTransformGestures(
                                onGesture = { centroid, pan, gestureZoom, _ ->
                                    val currentTime = System.currentTimeMillis()

                                    // Handle zoom
                                    val newScale = (scale * gestureZoom).coerceIn(1f, 3f)

                                    if (newScale > 1f) {
                                        // When zoomed in, handle pan within the zoomed image
                                        val maxX = (page.width * (newScale - 1) / 2)
                                        val maxY = (page.height * (newScale - 1) / 2)

                                        offsetX = (offsetX + pan.x).coerceIn(-maxX, maxX)
                                        offsetY = (offsetY + pan.y).coerceIn(-maxY, maxY)
                                    } else {
                                        // When not zoomed in, allow vertical scrolling
                                        val deltaY = pan.y - previousOffset.y

                                        // Only trigger scroll if enough time has passed since last scroll
                                        if (currentTime - lastPanTime > 16 && abs(deltaY) > scrollThreshold) {
                                            scrollJob?.cancel()
                                            scrollJob = coroutineScope.launch {
                                                scrollState.scrollBy(-deltaY * 2)
                                            }
                                            lastPanTime = currentTime
                                        }
                                    }

                                    scale = newScale
                                    previousOffset = Offset(pan.x, pan.y)

                                    // Reset offsets when returning to normal scale
                                    if (scale <= 1f) {
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            )
                        }
                )
            }
        }
    }
}