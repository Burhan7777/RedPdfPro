package com.pzbdownloaders.redpdfpro.core.presentation.Component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R

@Composable
fun AlertDialogBox(
    name: MutableState<String> = mutableStateOf(""),
    path: String = "",
    listOfPdfs: SnapshotStateList<String> = mutableStateListOf(),
    pageNumbersSelected: ArrayList<Int> = ArrayList(),
    onDismiss: () -> Unit,
    featureExecution: () -> Unit

) {

    var showFeature by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
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
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = {
                    Text(
                        text = stringResource(id = R.string.saveAs),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 15.sp
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                )
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.value.isNotEmpty()) {
                        featureExecution()
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    color = MaterialTheme.colorScheme.onSecondary
                )
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
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}