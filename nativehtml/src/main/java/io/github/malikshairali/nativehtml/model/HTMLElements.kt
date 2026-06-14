package io.github.malikshairali.nativehtml.model

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.malikshairali.nativehtml.LocalHtmlUrlClickHandler

private const val URL_ANNOTATION_TAG = "URL"

sealed class HTMLElement {
    @Composable
    abstract fun render()
}

interface InlineHTMLElement {
    fun appendToBuilder(builder: AnnotatedString.Builder)
}

@Composable
private fun RenderAnnotatedText(
    annotatedText: AnnotatedString,
    style: TextStyle,
    modifier: Modifier = Modifier,
    onUrlClick: (String) -> Unit
) {
    val hasLink = annotatedText.getStringAnnotations(
        tag = URL_ANNOTATION_TAG,
        start = 0,
        end = annotatedText.length
    ).isNotEmpty()

    if (!hasLink) {
        Text(
            text = annotatedText,
            style = style,
            modifier = modifier
        )
    } else {
        @Suppress("DEPRECATION")
        ClickableText(
            text = annotatedText,
            style = style,
            modifier = modifier,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = URL_ANNOTATION_TAG,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    onUrlClick(annotation.item)
                }
            }
        )
    }
}

data class TextElement(
    val text: String,
    val href: String? = null,
    val style: TextStyle
) : HTMLElement(), InlineHTMLElement {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        if (href == null) {
            builder.withStyle(style.toSpanStyle()) {
                append(text)
            }
        } else {
            builder.apply {
                pushStringAnnotation(URL_ANNOTATION_TAG, href)
                val linkColor = if (style.color == Color.Unspecified) Color(0xFF1565C0) else style.color
                withStyle(style.toSpanStyle().merge(SpanStyle(color = linkColor, textDecoration = style.textDecoration ?: TextDecoration.Underline))) {
                    append(text)
                }
                pop()
            }
        }
    }

    @Composable
    override fun render() {
        val handleUrlClick = LocalHtmlUrlClickHandler.current
        Text(
            text = text,
            style = style,
            modifier = Modifier.then(
                href?.let { url ->
                    Modifier.clickable {
                        handleUrlClick(url)
                    }
                } ?: Modifier
            )
        )
    }
}

data class InlineCode(val text: String) : HTMLElement() {
    @Composable
    override fun render() {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.background(Color.LightGray).padding(4.dp)
        )
    }
}

data class Blockquote(val text: String) : HTMLElement() {
    @Composable
    override fun render() {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            )
            Text(
                text = text,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

data class ListItem(val children: List<HTMLElement>) : HTMLElement() {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun render() {
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            children.forEach { it.render() }
        }
    }
}

data class UnorderedList(val items: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            items.forEach { item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "• ",
                        fontWeight = FontWeight.Bold,
                    )
                    Box(modifier = Modifier.weight(1f)) { item.render() }
                }
            }
        }
    }
}

data class OrderedList(val items: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            items.forEachIndexed { index, item ->
                Row(verticalAlignment = Alignment.Top) {
                    Text("${index + 1}. ", fontWeight = FontWeight.Bold)
                    Box(modifier = Modifier.weight(1f)) { item.render() }
                }
            }
        }
    }
}

data object LineBreak : HTMLElement() {
    @Composable
    override fun render() {
        Spacer(modifier = Modifier.height(4.dp))
    }
}

data class Image(val src: String?, val alt: String) : HTMLElement() {
    @Composable
    override fun render() {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(src).crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = alt,
            modifier = Modifier.fillMaxSize()
        )
    }
}

data class Table(val rows: List<TableRow>) : HTMLElement() {
    @Composable
    override fun render() {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            rows.forEach { it.render() }
        }
    }
}

data class TableRow(val cells: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            cells.forEach { cell ->
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, Color.Gray)
                ) {
                    cell.render()
                }
            }
        }
    }
}

data class TableCell(val children: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Row(modifier = Modifier.padding(8.dp)) {
            children.forEach { it.render() }
        }
    }
}

data class TableHeaderCell(val children: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(Color(0xFFF5F5F5))
        ) {
            children.forEach { it.render() }
        }
    }
}

data class Paragraph(
    val children: List<HTMLElement>,
    val style: TextStyle
) : HTMLElement() {
    @Composable
    override fun render() {
        val handleUrlClick = LocalHtmlUrlClickHandler.current
        var textBuilder = AnnotatedString.Builder()

        children.forEach { child ->
            if (child is InlineHTMLElement) {
                child.appendToBuilder(textBuilder)
            } else {
                val annotated = textBuilder.toAnnotatedString()
                RenderAnnotatedText(
                    annotatedText = annotated,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    style = style,
                    onUrlClick = handleUrlClick
                )
                child.render()
                textBuilder = AnnotatedString.Builder()
            }
        }

        if (textBuilder.length != 0) {
            val annotated = textBuilder.toAnnotatedString()
            RenderAnnotatedText(
                annotatedText = annotated,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                style = style,
                onUrlClick = handleUrlClick
            )
        }
    }
}

data class Span(
    val children: List<HTMLElement>,
    val style: TextStyle = TextStyle()
) : HTMLElement(), InlineHTMLElement {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        children.forEach { child ->
            if (child is InlineHTMLElement) {
                child.appendToBuilder(builder)
            }
        }
    }

    @Composable
    override fun render() {
        val handleUrlClick = LocalHtmlUrlClickHandler.current
        val annotated = buildAnnotatedString {
            children.forEach { child ->
                if (child is InlineHTMLElement) {
                    child.appendToBuilder(this)
                }
            }
        }
        RenderAnnotatedText(
            annotatedText = annotated,
            style = style,
            onUrlClick = handleUrlClick
        )
    }
}


data class Div(val children: List<HTMLElement>) : HTMLElement() {
    @Composable
    override fun render() {
        Column(modifier = Modifier.fillMaxWidth()) {
            children.forEach { child ->
                child.render()
            }
        }
    }
}

data object HorizontalRule : HTMLElement() {
    @Composable
    override fun render() {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.LightGray)
        )
    }
}

data class PreformattedText(val text: String, val style: TextStyle) : HTMLElement() {
    @Composable
    override fun render() {
        Text(
            text = text,
            style = style,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(8.dp)
        )
    }
}


data class UnsupportedHtml(val rawHtml: String) : HTMLElement() {
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun render() {
        val fullHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { margin: 0; padding: 0; background: #000; }
                    iframe, video, embed { width: 100% !important; height: 100% !important; }
                </style>
            </head>
            <body>$rawHtml</body>
            </html>
        """.trimIndent()
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.cacheMode = WebSettings.LOAD_DEFAULT
                    settings.domStorageEnabled = true
                    loadDataWithBaseURL(
                        "https://www.youtube.com",
                        fullHtml,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )
    }
}