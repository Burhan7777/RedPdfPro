package com.pzbdownloaders.redpdfpro.extractimagefeature

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.extractimagefeature.util.extractImagesFromPDFWithPdfium
import com.pzbdownloaders.redpdfpro.splitpdffeature.components.SingleRowSplitFeature
import com.pzbdownloaders.redpdfpro.splitpdffeature.screens.getPdfs
import com.pzbdownloaders.redpdfpro.splitpdffeature.utils.getFilePathFromContentUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ExtractImage(
    activity: MainActivity,
    viewModel: MyViewModel,
    navHostController: NavHostController
) {
    var listOfPdfs = ArrayList<Uri>()
    var totalPages by remember { mutableStateOf(0) }
    val path = remember { mutableStateOf("") }


    val scope = rememberCoroutineScope()

    var queryForSearch = remember { mutableStateOf("") }
    val context = LocalContext.current
    val stroke = Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    val result = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                path.value = getFilePathFromContentUri(it, activity)!!
                extractImagesFromPDFWithPdfium(File(path.value), context, scope)

//
            }
        })

    LaunchedEffect(key1 = true) {
        getPdfs(
            listOfPdfs,
            activity,
            viewModel.listOfPdfNames,
            viewModel.listOfSize
        )
        withContext(Dispatchers.Main) {
            viewModel.mutableStateListOfPdfs = listOfPdfs.toMutableStateList()
            println("QUERY:${viewModel.mutableStateListOfPdfs[0].host}")
        }
    }

    val filteredPdfs = remember(queryForSearch.value, viewModel.mutableStateListOfPdfs) {
        if (queryForSearch.value.isBlank()) {
            viewModel.mutableStateListOfPdfs // Show all files if the query is empty
        } else {
            viewModel.mutableStateListOfPdfs.filterIndexed { index, _ ->
                viewModel.listOfPdfNames[index].contains(queryForSearch.value, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            androidx.compose.material.OutlinedTextField(
                value = queryForSearch.value,
                onValueChange = { queryForSearch.value = it },
                label = { Text("Search PDFs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(10.dp),
                    topEnd = CornerSize(10.dp),
                    bottomEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp),
                )
            )


            LazyColumn() {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                            .clickable {
                                result.launch("application/pdf")
                            }
                            .drawBehind {
                                drawRoundRect(
                                    color = Color.Red,
                                    style = stroke,
                                    cornerRadius = CornerRadius(10.dp.toPx())
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.upload),
                                contentDescription = stringResource(
                                    id = R.string.upload
                                ),
                                modifier = Modifier
                                    .padding(top = 20.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.addPDF),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                            )
                        }
                    }
                }

                itemsIndexed(items = filteredPdfs) { index, item ->
                    val originalIndex = viewModel.mutableStateListOfPdfs.indexOf(item)
                    if (originalIndex != -1) {
                        SingleRowSplitFeature(
                            uri = item,
                            nameOfPdfFile = viewModel.listOfPdfNames[originalIndex],
                            activity = activity,
                            navHostController = navHostController,
                            viewModel = viewModel
                        )
                    } else {

                    }
                }
//                itemsIndexed(items = viewModel.mutableStateListOfPdfs) { index, item ->
//
//                    SingleRowSplitFeature(
//                        uri = item,
//                        nameOfPdfFile = viewModel.listOfPdfNames[index],
//                        activity = activity,
//                        navHostController = navHostController,
//                        viewModel = viewModel
//                    )
//                }
            }
        }
    }
}
