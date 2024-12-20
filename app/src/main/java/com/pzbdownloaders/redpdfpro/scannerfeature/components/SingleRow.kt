package com.pzbdownloaders.redpdfpro.scannerfeature.components

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.documentfeature.util.savePdfAsImageInTempFolder
import com.pzbdownloaders.redpdfpro.scannerfeature.util.ScannerModel
import com.pzbdownloaders.redpdfpro.scannerfeature.util.downloadPdfAsJpeg
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun SingleRowScannerMainScreen(
    modelScanner: ScannerModel,
    showCircularProgress: MutableState<Boolean>,
    nameOfWordFile: MutableState<String>,
    pathOfPdfFile: MutableState<String>,
    showWordFIleSaveDialogBox: MutableState<Boolean>,
    showTextFileSaveDialogBox: MutableState<Boolean>,
    activity: MainActivity,
    showBottomSheet: MutableState<Boolean>,
    bitmapOfPdfFile: MutableState<Bitmap?>,
    nameOfPdfFIle: MutableState<String?>,
    showShareDialogBox: MutableState<Boolean>,
    shareFileAsPdf: MutableState<Boolean>,
    shareFileAsImage: MutableState<Boolean>,
    rememberFilePath: MutableState<String>,
    showConvertingIntoImagesProgressDialogBox: MutableState<Boolean>,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                var uri = FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName + ".provider",
                    File(modelScanner.path!!)
                )
                navController.navigate(
                    Screens.PdfViewer.pdfViewerWIthUri(
                        uri.toString(),
                        modelScanner.path
                    )
                )
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 10.dp,
            focusedElevation = 20.dp
        )
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .border(
                    1.dp,
                    color = MaterialTheme.colorScheme.onSecondary
                ),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(modelScanner.bitmap).crossfade(true).build(),
                contentDescription = "Pdf Image",
                modifier = Modifier
                    .height(150.dp)
                    .width(120.dp),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .height(150.dp),
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = modelScanner.name ?: "",
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                            .align(Alignment.TopStart),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 5.dp)
                    ) {
                        if (shareFileAsPdf.value) {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                var uri = FileProvider.getUriForFile(
                                    context,
                                    context.applicationContext.packageName + ".provider",
                                    File(rememberFilePath.value)
                                )
                                putExtra(Intent.EXTRA_STREAM, uri)
                                activity.startActivity(this)
                                shareFileAsPdf.value = false
                            }
                        }
                        var listOfUris = ArrayList<Uri>()
                        var listOfBitmaps: ArrayList<String>

                        if (shareFileAsImage.value) {
                            scope.launch(Dispatchers.IO) {
                                showShareDialogBox.value = false
                                showConvertingIntoImagesProgressDialogBox.value = true
                                listOfBitmaps = savePdfAsImageInTempFolder(rememberFilePath.value)

                                for (i in listOfBitmaps.indices) {
                                    listOfUris.add(
                                        FileProvider.getUriForFile(
                                            context,
                                            context.applicationContext.packageName + ".provider",
                                            File(listOfBitmaps[i])
                                        )
                                    )
                                }
                                showConvertingIntoImagesProgressDialogBox.value = false
                                Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    type = "image/*"
                                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, listOfUris)
                                    activity.startActivity(this)
                                    //println(shareFileAsImage.value)
                                }
                            }
                        }
                        shareFileAsImage.value = false
                        IconButton(onClick = {
                            showShareDialogBox.value = true
                            rememberFilePath.value = modelScanner.path!!

                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share file"
                            )
                        }
                        IconButton(onClick = {
                            showCircularProgress.value = true
                            scope.launch(Dispatchers.IO) {
                                val result = downloadPdfAsJpeg(modelScanner.path!!, context)
                                withContext(Dispatchers.Main) {
                                    if (result == "Done")
                                        Toast.makeText(
                                            context,
                                            " Images saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    showCircularProgress.value = false
                                }
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Download as Image",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            println("hello")
                            pathOfPdfFile.value = modelScanner.path!!
                            showWordFIleSaveDialogBox.value = true

                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.word),
                                contentDescription = "Save to word file",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            pathOfPdfFile.value = modelScanner.path!!
                            showTextFileSaveDialogBox.value = true
                        }) {
                            Icon(
                                painterResource(id = R.drawable.text_scanner),
                                contentDescription = "Save as text file",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            pathOfPdfFile.value = modelScanner.path!!
                            bitmapOfPdfFile.value = modelScanner.bitmap
                            nameOfPdfFIle.value = modelScanner.name
                            showBottomSheet.value = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }


}