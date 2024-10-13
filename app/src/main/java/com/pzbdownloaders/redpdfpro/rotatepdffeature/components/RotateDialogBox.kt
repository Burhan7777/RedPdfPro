package com.pzbdownloaders.redpdfpro.rotatepdffeature.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chaquo.python.Python
import com.pzbdownloaders.redpdfpro.R

@Composable
fun RotateDialogBox(
    name: MutableState<String> = mutableStateOf(""),
    showAlertDialog: MutableState<Boolean>,
    selectedRotateAngle: MutableState<Int>,
    options: Array<String>,
    onDismiss: () -> Unit,
    featureExecution: () -> Unit

) {


    var showFeature by remember {
        mutableStateOf(false)
    }

    var radioSelected by remember {
        mutableStateOf("")
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
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                Text(
                    text = stringResource(id = R.string.rotateByAngle),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                options.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = radioSelected == it,
                            onClick = { radioSelected = it },
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = MaterialTheme.colorScheme.onPrimary,
                                selectedColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Text(text = it, fontSize = 18.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedRotateAngle.value = radioSelected.toInt()
                    showAlertDialog.value = !showAlertDialog.value
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