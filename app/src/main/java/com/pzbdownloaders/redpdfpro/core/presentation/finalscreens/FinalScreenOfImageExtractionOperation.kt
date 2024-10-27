package com.pzbdownloaders.redpdfpro.core.presentation.finalscreens

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.pzbdownloaders.redpdfpro.R
import com.pzbdownloaders.redpdfpro.core.presentation.MainActivity
import com.pzbdownloaders.redpdfpro.core.presentation.MyViewModel
import com.pzbdownloaders.redpdfpro.core.presentation.Screens
import java.io.File

@Composable
fun FinalScreenForImageExtraction(
    activity: MainActivity,
    navHostController: NavHostController,
    viewModel: MyViewModel
) {

    BackHandler {
        viewModel.listOfImagesFromExtractImages.clear()
        navHostController.navigateUp()
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
        val showConvertingIntoImagesDialogBox = remember {
            mutableStateOf(
                false
            )
        }
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.folder),
                contentDescription = stringResource(
                    id = R.string.folder
                )
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "downloads/Pro Scanner/images")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    type = "image/*"
                }
                activity.startActivity(intent)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
        ) {
            Text(text = stringResource(id = R.string.openGallery), color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                val uris = viewModel.listOfImagesFromExtractImages.map { path ->
                    FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        File(path)
                    )
                }

                val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    type = "image/*"
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                context.startActivity(Intent.createChooser(shareIntent, "Share images via"))
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSecondary,

                ),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
        ) {
            Text(
                text = stringResource(id = R.string.shareImages),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(140.dp))
        Button(
            onClick = {
                viewModel.listOfImagesFromExtractImages.clear()
                navHostController.navigateUp()
            },
            modifier = Modifier
                .height(60.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSecondary

            )
        ) {

            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = stringResource(
                    id = R.string.closeScreen
                ),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FinalScreenForImageExtractionPreview() {
//    FinalScreenForImageExtraction()
//}