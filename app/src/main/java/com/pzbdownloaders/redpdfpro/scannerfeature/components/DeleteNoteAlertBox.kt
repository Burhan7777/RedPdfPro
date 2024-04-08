package com.pzbdownloaders.redpdfpro.scannerfeature.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel

@Composable
fun DeleteNoteAlertBox(
    viewModel: MyViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    onDismiss: () -> Unit,
    featureExecution: () -> Unit
) {

    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(onDismissRequest = {
        onDismiss()
    },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = MaterialTheme.colorScheme.primary,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Text(
                text = stringResource(id = R.string.deleteFile),
                fontSize = 20.sp
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.areYouSure),
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    featureExecution()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                Text(text = stringResource(id = R.string.delete))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
                colors = ButtonDefaults.buttonColors(
                ),
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}
