package io.github.malikshairali.nativehtml

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import io.github.malikshairali.nativehtml.model.HTMLElement
import io.github.malikshairali.nativehtml.parser.HTMLParser
import io.github.malikshairali.nativehtml.style.CssStyleBuilder
import io.github.malikshairali.nativehtml.style.StyleRegistry
import io.github.malikshairali.nativehtml.style.css

class NativeHTMLBuilder {
    internal val styleRegistry = StyleRegistry()
    internal val customRenderers = mutableMapOf<String, (org.jsoup.nodes.Element) -> HTMLElement>()

    fun style(tag: String, style: TextStyle) {
        styleRegistry.setStyle(tag, style)
    }

    fun style(tag: String, build: CssStyleBuilder.() -> Unit) {
        styleRegistry.setStyle(tag, css(build))
    }

    fun customTag(tag: String, renderer: (org.jsoup.nodes.Element) -> HTMLElement) {
        customRenderers[tag] = renderer
    }
}

internal val LocalHtmlUrlClickHandler = staticCompositionLocalOf<(String) -> Unit> { {} }

@Composable
fun NativeHTML(
    html: String,
    modifier: Modifier = Modifier,
    onLinkClick: ((String) -> Boolean)? = null,
    builder: NativeHTMLBuilder.() -> Unit = {}
) {
    val configuration = remember(builder) { NativeHTMLBuilder().apply(builder) }
    val parser = remember(configuration) { HTMLParser(configuration) }
    val elements = remember(html, parser) { parser.parse(html) }
    val context = LocalContext.current
    val urlClickHandler: (String) -> Unit = remember(context, onLinkClick) {
        { url ->
            val handledInApp = onLinkClick?.invoke(url) == true
            if (!handledInApp) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                try {
                    context.startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    // Ignore invalid or unsupported URLs gracefully.
                }
            }
        }
    }

    CompositionLocalProvider(LocalHtmlUrlClickHandler provides urlClickHandler) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(elements) { element ->
                element.render()
            }
        }
    }
}

@Deprecated("Use NativeHTML with DSL builder instead")
@Composable
fun RenderHtml(
    html: String,
    modifier: Modifier = Modifier
) {
    NativeHTML(html = html, modifier = modifier)
}
