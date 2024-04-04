package com.pzbdownloaders.scannerfeature.components.BottomSheet

import android.graphics.Bitmap
import android.os.Environment
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    bitmapOfPdfFile: MutableState<Bitmap?>,
    showRenameSaveDialogBox: MutableState<Boolean>,
    rename: MutableState<String>
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
            BottomSheetRenameItem(
                rename,
                showRenameSaveDialogBox,
                painterResource(id = R.drawable.rename),
                R.string.renamePDF,
                R.string.rename,
                showBottomSheet
            ) {
                val externalDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val pathWithName = "$externalDir/Pro Scanner/Pdfs/${nameOfPdfFIle.value}"
                val pathWithoutName =
                    "$externalDir/Pro Scanner/Pdfs/" // This is important so we can create a file in that directory. If we used pathWithName we would end up with new name which also has older name appended to it.
                val file = File(pathWithName)
                println(pathWithName)
                if (file.exists()) {
                    val renameFile = File(pathWithoutName, "${rename.value}.pdf")
                    file.renameTo(renameFile)
                    viewModel.listOfFiles.add(renameFile)
                    viewModel.addItem()
                    var uri = FileProvider.getUriForFile(
                        activity,
                        activity.applicationContext.packageName + ".provider",
                        File(pathOfPdfFile.value)
                    )
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
            BottomSheetSplitItem(
                navHostController,
                painterResource(id = R.drawable.split_bottom_sheet),
                R.string.splitThePDF,
                R.string.splitPdf,
                pathOfPdfFile
            )
            BottomSheetMergeItem(
                contentDescriptionId = R.string.mergePdf,
                nameId = R.string.mergePdf,
                painter = painterResource(id = R.drawable.merge_bottom_sheet),
                navHostController = navHostController,
                pathOfPdf = pathOfPdfFile,
                nameOfPdf = nameOfPdfFIle
            )
        }
    }
}