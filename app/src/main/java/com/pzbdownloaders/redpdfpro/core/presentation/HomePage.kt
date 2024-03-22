package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.FeatureBox

@Composable
fun HomePage(navHostController: NavHostController, viewModel: MyViewModel) {
    viewModel.modelList.clear()
    viewModel.listOfPdfToMerge.clear()
    viewModel.pdfNames.clear()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = stringResource(id = R.string.tools),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            FeatureBox(
                stringId = R.string.splitPdf,
                drawableId = R.drawable.split,
                contentDescription = stringResource(id = R.string.splitPdf),
                navHostController,
                Screens.SplitPdf.route,
            )

            FeatureBox(
                stringId = R.string.mergePdf,
                drawableId = R.drawable.merge,
                contentDescription = stringResource(id = R.string.mergePdf),
                navHostController,
                Screens.MergePdf.route
            )
            FeatureBox(
                stringId = R.string.rotatePDF,
                drawableId = R.drawable.rotate,
                contentDescription = stringResource(id = R.string.rotatePDF),
                navHostController,
                Screens.RotatePdf.route
            )

        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.extractFromPdf),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            FeatureBox(
                stringId = R.string.extractText,
                drawableId = R.drawable.text,
                contentDescription = stringResource(id = R.string.extractText),
                navHostController,
                Screens.ExtractText.route,
            )

        }

    }
}