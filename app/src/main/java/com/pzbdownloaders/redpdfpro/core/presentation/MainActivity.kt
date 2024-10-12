package com.pzbdownloaders.redpdfpro.core.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.pzbdownloaders.redpdfpro.ui.theme.RedPdfProTheme
import cz.adaptech.tesseract4android.BuildConfig
import java.io.File

class MainActivity : ComponentActivity() {
    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        var permission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            }


        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Intent().apply {
                    action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    val uri =
                        Uri.parse("package:$packageName")
                    this.data = uri
                }.also {
                    startActivityForResult(it, 100)
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                loadPdfs()
            } else {
                permission.launch(
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    )
                )
            }
        }

        setContent {
            RedPdfProTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {


                    MainScreen(
                        navHostController = rememberNavController(),
                        viewModel = viewModel,
                        activity = this@MainActivity
                    )
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, resultData: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 100) {

            if (Build.VERSION.SDK_INT >= 30) {
                if (Environment.isExternalStorageManager()) {
                    loadPdfs()
                }
            } else {
                Toast.makeText(
                    this,
                    "App needs this permission to function properly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            loadPdfs()
            Toast.makeText(this, "App", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadPdfs() {
        var externalDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File("$externalDir/Pro Scanner/Scanned Pdfs")
        if (!file.exists()) {
            file.mkdirs()
        }
        val filePdf = File("$externalDir/Pro Scanner/Pdfs")
        if (!filePdf.exists()) {
            filePdf.mkdirs()
        }
        //oast.makeText(this, "App", Toast.LENGTH_SHORT).show()
        if (viewModel.listOfFiles.size < (file.listFiles()?.size ?: 0)) {
            viewModel.listOfFiles =
                file.listFiles()?.toCollection(ArrayList()) ?: ArrayList<File>()
            viewModel.listOfFiles.reverse()
            viewModel.getImage()
        }
    }
}