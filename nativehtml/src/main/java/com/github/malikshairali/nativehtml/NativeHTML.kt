package com.github.malikshairali.nativehtml

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun RenderHtml(
    html: String,
    modifier: Modifier = Modifier
) {
    val elements = remember { HTMLParser().parse(html) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(elements) { element ->
            element.toCompose().invoke()
        }
    }
}