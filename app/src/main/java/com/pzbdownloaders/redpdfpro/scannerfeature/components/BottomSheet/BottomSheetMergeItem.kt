package com.pzbdownloaders.redpdfpro.scannerfeature.components.BottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.Screens

@Composable
fun BottomSheetMergeItem(
    contentDescriptionId: Int,
    nameId: Int,
    painter: Painter,
    navHostController: NavHostController,
    pathOfPdf: MutableState<String>,
    nameOfPdf: MutableState<String?>

) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable {
                navHostController.navigate(
                    Screens.MergePdf.mergePDfWithArguments(
                        nameOfPdf.value!!,
                        pathOfPdf.value
                    )
                )
            }
            .padding(top = 7.dp, bottom = 7.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = stringResource(id = contentDescriptionId),
                modifier = Modifier.padding(
                    start = 30.dp, top = 10.dp
                )
            )
            Text(
                text = stringResource(id = nameId),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}