package com.pzbdownloaders.redpdfpro.documentfeature.components

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.scannerfeature.components.SavePdfAsDocxFile
import com.pzbdownloaders.redpdfpro.scannerfeature.util.ScannerModel
import com.pzbdownloaders.redpdfpro.scannerfeature.util.downloadPdfAsJpeg
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun SingleRowDocumentFeature(
    /*
        showWordFIleSaveDialogBox: MutableState<Boolean>,
        showTextFileSaveDialogBox: MutableState<Boolean>,

        showBottomSheet: MutableState<Boolean>*/
    uri: Uri,
    nameOfPdfFile: String,
    activity: MainActivity,
    showCircularProgress: MutableState<Boolean>,
    viewModel: MyViewModel,
    pathOfThePdfFile: MutableState<String>,
    saveWordFIleDialogBox: MutableState<Boolean>,
    showBottomSheet: MutableState<Boolean>,
    nameOfPdfFileOutsideScope: MutableState<String?>
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pdf_document_feature),
                contentDescription = "Pdf",
            )
            Column(
                modifier = Modifier
                    .requiredHeight(100.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = nameOfPdfFile,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                            .align(Alignment.TopStart),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 5.dp, top = 5.dp)
                    ) {
                        IconButton(onClick = {
                            Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                var uri = uri
                                putExtra(Intent.EXTRA_STREAM, uri)
                                activity.startActivity(this)

                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share file"
                            )
                        }
                        IconButton(onClick = {
                            showCircularProgress.value = true
                            scope.launch(Dispatchers.Default) {
                                var file = getFilePathFromContentUri(uri, activity = activity)
                                val result = downloadPdfAsJpeg(file!!, activity)
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
                                tint = Color.Unspecified
                            )
                        }
                        IconButton(onClick = {
                            var file = getFilePathFromContentUri(uri, activity = activity)
                            pathOfThePdfFile.value = file!!
                            saveWordFIleDialogBox.value = true


                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.word),
                                contentDescription = "Save to word file",
                                tint = Color.Unspecified
                            )
                        }
                        IconButton(onClick = {
                            /* pathOfPdfFile.value = modelScanner.path!!
                             showTextFileSaveDialogBox.value = true*/
                        }) {
                            Icon(
                                painterResource(id = R.drawable.text_scanner),
                                contentDescription = "Save as text file"
                            )
                        }
                        IconButton(onClick = {
                            /*      pathOfPdfFile.value = modelScanner.path!!
                                  bitmapOfPdfFile.value = modelScanner.bitmap
                                  nameOfPdfFIle.value = modelScanner.name
                                  showBottomSheet.value = true*/
                            showBottomSheet.value = true
                            pathOfThePdfFile.value = getFilePathFromContentUri(uri, activity)!!
                            nameOfPdfFileOutsideScope.value = nameOfPdfFile
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                    }
                }
            }
        }
    }


}