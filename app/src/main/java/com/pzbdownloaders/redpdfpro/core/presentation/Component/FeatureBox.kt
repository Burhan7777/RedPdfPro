package com.pzbdownloaders.redpdfpro.core.presentation.Component

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.splitpdf.components.modelBitmap

@Composable
fun FeatureBox(
    stringId: Int,
    drawableId: Int,
    contentDescription: String,
    navHostController: NavHostController,
    route: String,
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(110.dp)
            .clip(
                shape = MaterialTheme.shapes.medium.copy(
                    all = CornerSize(10.dp)
                )
            )
            .background(MaterialTheme.colorScheme.primary)
            .padding(all = 10.dp)
            .clickable {
                navHostController.navigate(route)
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(
                    id = drawableId
                ),
                contentDescription = contentDescription,
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(id = stringId),
                modifier = Modifier,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}