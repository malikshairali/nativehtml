package com.github.malikshairali.nativehtml.model

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.malikshairali.nativehtml.HtmlTagHandler
import com.github.malikshairali.nativehtml.style.HeadingStyleRegistry
import org.jsoup.nodes.Element


sealed class HTMLElement {
    abstract fun toCompose(): @Composable () -> Unit
}

data class Heading(private val level: Int, val text: String) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text,
            style = HeadingStyleRegistry.getStyle(level),
            modifier = Modifier.padding(8.dp).fillMaxSize()
        )
    }
}

class HeadlineTagHandler(): HtmlTagHandler {
    override fun handle(element: Element): HTMLElement {
        return Heading(element.tagName().substring(1).toInt(), element.text())
    }

    override fun canHandle(tagName: String): Boolean {
        return tagName.contains("h")
    }

}