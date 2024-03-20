package com.pzbdownloaders.redpdfpro.splitpdffeature.screens

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.AlertDialogBox
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRow
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.modelBitmap
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.loadPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun SplitPdf(navHostController: NavHostController, activity: MainActivity, viewModel: MyViewModel) {

    val context = LocalContext.current
    var pageNumbersSelected = remember { mutableStateOf(ArrayList<Int>()) }
    var path by remember {
        mutableStateOf(" ")
    }

    var name = remember {
        mutableStateOf("")
    }
    var showAlertBox by remember { mutableStateOf(false) }


    var darkTheme = isSystemInDarkTheme()

    var totalPages by remember { mutableStateOf(0) }
    lateinit var pdfRenderer: PdfRenderer
    lateinit var parcelFileDescriptor: ParcelFileDescriptor
    var file by remember { mutableStateOf<File?>(null) }

    var result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            path = getFilePathFromContentUri(it!!, activity)!!
            file = File(path)
            val python = Python.getInstance()
            val module = python.getModule("splitPDF")
            totalPages = module.callAttr("total_pages", path).toString().toInt()
        })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (totalPages == 0) {
                Button(onClick = { result.launch("application/pdf") }) {
                    Text(text = stringResource(id = R.string.addPDF))
                }
            }


            if (totalPages > 0 && file != null) {
                parcelFileDescriptor =
                    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                pdfRenderer = PdfRenderer(parcelFileDescriptor)
                LazyColumnVer(
                    totalPages = totalPages.toString().toInt(),
                    context = context,
                    file = file!!,
                    pdfRenderer,
                    pageNumbersSelected.value,
                    darkTheme,
                    viewModel
                )
            }
        }
        if (file != null) {
            Button(
                onClick = {
                    showAlertBox = !showAlertBox
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.BottomCenter),
                shape = MaterialTheme.shapes.medium.copy(
                    all = CornerSize(10.dp)
                )
            ) {
                Text(text = stringResource(id = R.string.splitPdf))
            }
        }
    }
    if (showAlertBox)
        AlertDialogBox(
            name = name,
            path = path,
            pageNumbersSelected = pageNumbersSelected.value
        ) {
            showAlertBox = false
        }

}

@Composable
fun LazyColumnVer(
    totalPages: Int,
    context: Context,
    file: File,
    pdfRenderer: PdfRenderer,
    pageNoSelected: ArrayList<Int>,
    darkTheme: Boolean,
    viewModel: MyViewModel
) {


    var scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        for (i in 0 until totalPages) {
            var bitmap = loadPage(
                context,
                file,
                i,
                pdfRenderer,
                darkTheme
            )
            withContext(Dispatchers.Main) {
                if (viewModel.modelList.size < totalPages)
                    viewModel.modelList.add(modelBitmap(bitmap, isSelected = mutableStateOf(false)))
            }
        }
    }



    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(items = viewModel.modelList) { index, item ->
            SingleRow(model = item, pageNo = index, pageNoSelected)
        }

    }
}


