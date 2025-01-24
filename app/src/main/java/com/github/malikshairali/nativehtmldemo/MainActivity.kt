package com.github.malikshairali.nativehtmldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.github.malikshairali.nativehtml.RenderHtml
import com.github.malikshairali.nativehtmldemo.ui.theme.NativeHtmlComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            NativeHtmlComposeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RenderHtml(
                        html = "<h1>Hello World!</h1>",
                        modifier = Modifier.padding()
                    )
//                }
            }
        }
    }
}