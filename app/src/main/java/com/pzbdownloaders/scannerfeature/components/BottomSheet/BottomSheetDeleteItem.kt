package com.pzbdownloaders.scannerfeature.components.BottomSheet

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.scannerfeature.components.DeleteNoteAlertBox

@Composable
fun BottomSheetDeleteItem(
    showDeleteDialogBox: MutableState<Boolean>,
    viewModel: MyViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    imageVector: ImageVector,
    contentDescriptionId: Int,
    nameId: Int,
    showBottomSheet: MutableState<Boolean>,
    deleteFile: () -> Unit
) {
    if (showDeleteDialogBox.value) {
        DeleteNoteAlertBox(
            viewModel = viewModel,
            activity = activity,
            navHostController = navHostController,
            onDismiss = { showDeleteDialogBox.value = false }) {
            deleteFile()
            showDeleteDialogBox.value = false
            showBottomSheet.value = false
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable { showDeleteDialogBox.value = true }
            .padding(top = 7.dp, bottom = 7.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
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