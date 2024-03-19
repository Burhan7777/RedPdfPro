package com.pzbdownloaders.redpdfpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.pzbdownloaders.redpdfpro.core.presentation.MyNavHost
import com.pzbdownloaders.redpdfpro.ui.theme.RedPdfProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedPdfProTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    MyNavHost(rememberNavController())
                }
            }
        }
    }
}