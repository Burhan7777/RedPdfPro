package com.pzbdownloaders.redpdfpro.documentfeature.util

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbdownloaders.redpdfpro.R

@Composable
fun ShareAsPdfOrImage(
    shareFIleAsPdf: MutableState<Boolean>,
    shareFileAsImages: MutableState<Boolean>,
    showShareDialogBox: MutableState<Boolean>
) {
    AlertDialog(onDismissRequest = { showShareDialogBox.value = false },
        confirmButton = {},
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.shareAsPdf),
                    Modifier
                        .clickable {
                            shareFIleAsPdf.value = true
                        }
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(start = 5.dp)
                        .clip(CircleShape)
                        .border(
                            0.dp,
                            Color.White, shape = CircleShape
                        ),
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(id = R.string.shareAsImages),
                    Modifier
                        .clickable {
                            shareFileAsImages.value = true
                        }
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(start = 5.dp)
                        .clip(CircleShape)
                        .border(
                            0.dp,
                            Color.White, shape = CircleShape
                        ), fontSize = 16.sp
                )
            }
        })
}