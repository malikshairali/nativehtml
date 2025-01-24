package com.github.malikshairali.nativehtml

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.malikshairali.nativehtml.model.Heading

@Composable
fun RenderHtml(
    html: String,
    modifier: Modifier = Modifier
) {
    val elements = HTMLParser().parse(html)

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(elements) { element ->
            element.toCompose().invoke()
        }
    }
}