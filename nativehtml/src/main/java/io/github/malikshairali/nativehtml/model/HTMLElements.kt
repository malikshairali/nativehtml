package io.github.malikshairali.nativehtml.model

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

sealed class HTMLElement {
    @Composable
    abstract fun render()
}

interface InlineHTMLElement {
    fun appendToBuilder(builder: AnnotatedString.Builder)
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
                val tag = "URL"
                pushStringAnnotation(tag, href)
                withLink(LinkAnnotation.Url(url = href)) {
                    withStyle(
                        SpanStyle(
                            color = Color.Blue, textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(text)
                    }
                }
                pop()
            }
        }
    }

    @Composable
    override fun render() {
        val context = LocalContext.current
        Text(
            text = text,
            style = style,
            modifier = Modifier.then(
                href?.let { url ->
                    Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
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
                Row {
                    Text(
                        text = "• ", // Bullet point
                        fontWeight = FontWeight.Bold,
                    )
                    item.render()
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
                Row {
                    Text("${index + 1}. ", fontWeight = FontWeight.Bold)
                    item.render()
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

data class TableRow(val cells: List<TableCell>) : HTMLElement() {
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

data class Paragraph(
    val children: List<HTMLElement>,
    val style: TextStyle
) : HTMLElement() {
    @Composable
    override fun render() {
        var textBuilder = AnnotatedString.Builder()

        children.forEach { child ->
            if (child is InlineHTMLElement) {
                child.appendToBuilder(textBuilder)
            } else {
                Text(
                    text = textBuilder.toAnnotatedString(),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    style = style
                )
                child.render()
                textBuilder = AnnotatedString.Builder()
            }
        }

        if (textBuilder.length != 0) {
            Text(
                text = textBuilder.toAnnotatedString(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                style = style
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
        Text(
            text = buildAnnotatedString {
                children.forEach { child ->
                    if (child is InlineHTMLElement) {
                        child.appendToBuilder(this)
                    }
                }
            },
            style = style
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


data class UnsupportedHtml(val rawHtml: String) : HTMLElement() {
    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    override fun render() {
        Surface(modifier = Modifier.fillMaxWidth()) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient() // Keeps navigation within WebView
                        settings.javaScriptEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false // Auto-play videos
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        loadDataWithBaseURL(
                            null,
                            rawHtml,
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}