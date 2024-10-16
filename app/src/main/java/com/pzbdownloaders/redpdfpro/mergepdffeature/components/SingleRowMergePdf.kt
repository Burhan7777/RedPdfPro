package com.pzbdownloaders.redpdfpro.mergepdffeature.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.mergepdffeature.util.getFileName
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SingleRowMergePdf(
    uri: Uri,
    nameOfPdfFile: String,
    activity: MainActivity,
    navHostController: NavHostController,
    viewModel: MyViewModel,
    index: Int,
    listOfMergedPdfs: SnapshotStateList<String>,
    showLockedDialogBox: MutableState<Boolean>,
    path: MutableState<String>
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                path.value = getFilePathFromContentUri(uri, activity)!!
                scope.launch(Dispatchers.Default) {
                    val python = Python.getInstance()
                    val module = python.getModule("checkLockStatus")
                    var result = module.callAttr("check_lock_status_pdf", path.value)
                    withContext(Dispatchers.Main) {
                        if (result.toString() == "Locked") {
                            showLockedDialogBox.value = true
                        } else if (result.toString() == "Unlocked") {
                            listOfMergedPdfs.add(
                                getFilePathFromContentUri(
                                    uri,
                                    activity = activity
                                )!!
                            )
                            viewModel.pdfNames.add(nameOfPdfFile)
                        }
                    }
                }


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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
            Text(
                text = nameOfPdfFile,
                modifier = Modifier
                    .padding(10.dp),
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}