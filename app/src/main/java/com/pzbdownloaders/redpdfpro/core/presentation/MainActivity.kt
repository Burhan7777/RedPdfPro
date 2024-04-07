package com.pzbdownloaders.redpdfpro.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.pzbdownloaders.redpdfpro.ui.theme.RedPdfProTheme

class MainActivity : ComponentActivity() {
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this));
        }
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
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
}