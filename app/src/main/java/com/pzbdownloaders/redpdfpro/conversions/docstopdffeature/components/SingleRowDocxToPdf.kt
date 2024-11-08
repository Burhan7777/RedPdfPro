package com.pzbdownloaders.redpdfpro.conversions.docstopdffeature.components

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.google.gson.Gson
import com.pzbdownloaders.redpdfpro.conversions.core.domain.models.InitializeJob
import com.pzbdownloaders.redpdfpro.conversions.core.domain.models.JobStatus
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUriForDocx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SingleRowDocxToPdf(
    uri: Uri,
    nameOfDocxFile: String,
    activity: MainActivity,
    navHostController: NavHostController,
    viewModel: MyViewModel,
    pathOfDocxFile: MutableState<String>,
    saveAsDialogBox: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                pathOfDocxFile.value = getFilePathFromContentUriForDocx(uri, activity)!!
                saveAsDialogBox.value = true
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .requiredHeight(50.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Column(modifier = Modifier.align(Alignment.TopStart)) {
                        Text(
                            text = nameOfDocxFile,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp, end = 10.dp),
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
