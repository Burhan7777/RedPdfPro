package com.pzbdownloaders.redpdfpro.splitpdf.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorSpace.Model
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun SingleRow(model: modelBitmap, pageNo: Int, pageNoSelected: ArrayList<Int>) {
    /*  var isSelected = remember {
          mutableStateOf(false)
      }*/
    val borderColor = if (model.isSelected.value) Color.Red else Color.Gray
    val borderWidth = if (model.isSelected.value) 3.dp else 1.dp
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable {
            model.toggle()
            if (model.isSelected.value) {
                pageNoSelected.add(pageNo)
            } else {
                pageNoSelected.remove(pageNo)
            }
        }) {
        Image(
            bitmap = model.bitmap?.asImageBitmap()!!,
            contentDescription = "pdf",
            modifier = Modifier
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp))
                )
                .clip(shape = MaterialTheme.shapes.medium.copy(all = CornerSize(10.dp)))
        )
        Text(
            text = "${pageNo + 1}",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp),
            fontSize = 8.sp
        )
    }
}
