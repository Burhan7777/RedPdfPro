package com.pzbdownloaders.scannerfeature.components.BottomSheet

import android.graphics.Bitmap
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.scannerfeature.util.ScannerModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    showBottomSheet: MutableState<Boolean>,
    showDeleteDialogBox: MutableState<Boolean>,
    viewModel: MyViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    pathOfPdfFile: MutableState<String>,
    nameOfPdfFIle: MutableState<String?>,
    bitmapOfPdfFile: MutableState<Bitmap?>
) {
    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            dragHandle = {
                BottomSheetDefaults.DragHandle(

                )
            },
            windowInsets = BottomSheetDefaults.windowInsets,
            modifier = Modifier.height(300.dp)
        ) {
            BottomSheetDeleteItem(
                showDeleteDialogBox,
                viewModel,
                activity,
                navHostController,
                Icons.Default.Delete,
                R.string.deletePdf,
                R.string.delete,
                showBottomSheet
            ) {
                var uri = FileProvider.getUriForFile(
                    activity,
                    activity.applicationContext.packageName + ".provider",
                    File(pathOfPdfFile.value)
                );
                activity.contentResolver.delete(uri, null, null)
                viewModel.modelScanner.remove(
                    ScannerModel(
                        nameOfPdfFIle.value,
                        bitmapOfPdfFile.value,
                        pathOfPdfFile.value
                    )
                )
            }
        }
    }
}