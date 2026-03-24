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
import io.github.malikshairali.nativehtml.parser.HTMLParser

internal val LocalHtmlUrlClickHandler = staticCompositionLocalOf<(String) -> Unit> { {} }

@Composable
fun RenderHtml(
    html: String,
    modifier: Modifier = Modifier,
    onLinkClick: ((String) -> Boolean)? = null
) {
    val elements = remember(html) { HTMLParser().parse(html) }
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