package com.pzbdownloaders.redpdfpro.core.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import java.io.File


@Composable
fun FinalScreenOfPdfOperations(navHostController: NavHostController, path: String, uri: String) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.success),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.success),
            contentDescription = stringResource(
                id = R.string.successImage
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.folder),
                contentDescription = stringResource(
                    id = R.string.folder
                )
            )
            Text(text = path)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                navHostController.navigate(Screens.PdfReader.pdfReaderWithUri("", uri))
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
        ) {
            Text(text = stringResource(id = R.string.openDocument), color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    // var uri = uriCurrent
                    val uri = FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName + ".provider",
                        File(path)
                    )
                    putExtra(Intent.EXTRA_STREAM, uri)
                    context.startActivity(this)
                }
            },
            modifier = Modifier
                .width(250.dp)
                .height(60.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
            )
        ) {
            Text(
                text = stringResource(id = R.string.shareDocument),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .width(250.dp)
                .height(60.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
            )
        ) {
            Text(
                text = stringResource(id = R.string.shareAsImages),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
        Button(
            onClick = {
                navHostController.popBackStack()
                navHostController.popBackStack()
            },
            modifier = Modifier
                .height(60.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
            )
        ) {

            Image(
                painter = painterResource(id = R.drawable.close),
                contentDescription = stringResource(
                    id = R.string.closeScreen
                )
            )
        }

    }
}