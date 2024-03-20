package com.pzbdownloaders.redpdfpro.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.Component.FeatureBox

@Composable
fun HomePage(navHostController: NavHostController, viewModel: MyViewModel) {
    viewModel.modelList.clear()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(70.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item() {
                FeatureBox(
                    stringId = R.string.splitPdf,
                    drawableId = R.drawable.split,
                    contentDescription = "Split PDF",
                    navHostController,
                    Screens.splitPdf.route,
                )
            }
            item() {
                FeatureBox(
                    stringId = R.string.mergePdf,
                    drawableId = R.drawable.merge,
                    contentDescription = "Merge PDF",
                    navHostController,
                    Screens.mergePdf.route
                )
            }

        }
    }
}