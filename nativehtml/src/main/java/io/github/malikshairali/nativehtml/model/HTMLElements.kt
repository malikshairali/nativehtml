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
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import io.github.malikshairali.nativehtml.style.HeadingStyleRegistry

sealed class HTMLElement {
    abstract fun toCompose(): @Composable () -> Unit
    open fun appendToBuilder(builder: AnnotatedString.Builder) {}
}

// Heading tags
data class Heading(val level: Int, val text: String) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text,
            style = HeadingStyleRegistry.getStyle(level),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
    }
}

// Paragraph tag
data class Paragraph(val children: List<HTMLElement>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = buildAnnotatedString {
                children.forEach { it.appendToBuilder(this) }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
        )
    }
}

// Text node
data class TextNode(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.append(text)
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(text = text)
    }
}

// Strong or bold text
data class BoldText(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

// Anchor (link)
data class Anchor(val href: String?, val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.apply {
            val tag = "URL"
            pushStringAnnotation(tag, href ?: "")
            withLink(LinkAnnotation.Url(url = href ?: "")) {
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

    override fun toCompose(): @Composable () -> Unit = {
        val context = LocalContext.current
        Text(text = text, color = Color.Blue, modifier = Modifier.clickable {
            href?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        })
    }
}

// Inline code
data class InlineCode(val text: String) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.background(Color.LightGray).padding(4.dp)
        )
    }
}

// Emphasized text
data class EmphasizedText(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(text = text, fontStyle = FontStyle.Italic)
    }
}

// Blockquote
data class Blockquote(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = Color.Gray)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
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

// List item
data class ListItem(val children: List<HTMLElement>) : HTMLElement() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun toCompose(): @Composable () -> Unit = {
        Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp)) {
            Text(
                text = "•", // Bullet point
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp) // Space between bullet and content
            )
            FlowRow {
                children.forEach { it.toCompose().invoke() }
            }
        }
    }
}

// Unordered list
data class UnorderedList(val items: List<HTMLElement>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            items.forEach { item -> item.toCompose().invoke() }
        }
    }
}

// Ordered list
data class OrderedList(val items: List<HTMLElement>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            items.forEachIndexed { index, item ->
                Row {
                    Text("${index + 1}. ", fontWeight = FontWeight.Bold)
                    item.toCompose().invoke() // Each item is a ListItem
                }
            }
        }
    }
}

// Line break
data object LineBreak : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Image tag
data class Image(val src: String?, val alt: String) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(src).crossfade(true).build(),
            contentScale = ContentScale.Crop,
            contentDescription = alt,
            modifier = Modifier.fillMaxSize()//.padding(8.dp)
        )
    }
}

// Table elements
data class Table(val rows: List<TableRow>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            rows.forEach { it.toCompose().invoke() }
        }
    }
}

data class TableRow(val cells: List<TableCell>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            cells.forEach { cell ->
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().border(1.dp, Color.Gray)
                ) {
                    cell.toCompose().invoke()
                }
            }
        }
    }
}

data class TableCell(val children: List<HTMLElement>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Row(modifier = Modifier.padding(8.dp)) {
            children.forEach { it.toCompose().invoke() }
        }
    }
}

// Span
data class Span(val children: List<HTMLElement>) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        children.forEach { it.appendToBuilder(builder) }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = buildAnnotatedString {
                children.forEach { it.appendToBuilder(this) }
            }
        )
    }
}

// Mark
data class Mark(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(color = Color.Yellow)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text, modifier = Modifier.background(Color.Yellow)
        )
    }
}

// Subscript
data class Subscript(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(baselineShift = BaselineShift.Subscript)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text,
            fontSize = 12.sp,
            style = TextStyle(baselineShift = BaselineShift.Subscript)
        )
    }
}

// Superscript
data class Superscript(val text: String) : HTMLElement() {
    override fun appendToBuilder(builder: AnnotatedString.Builder) {
        builder.withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
            append(text)
        }
    }

    override fun toCompose(): @Composable () -> Unit = {
        Text(
            text = text,
            fontSize = 12.sp,
            style = TextStyle(baselineShift = BaselineShift.Superscript)
        )
    }
}

data class Div(val children: List<HTMLElement>) : HTMLElement() {
    override fun toCompose(): @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxWidth()) {
            children.forEach { child ->
                child.toCompose().invoke()
            }
        }
    }
}


data class UnsupportedHtml(val rawHtml: String) : HTMLElement() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun toCompose(): @Composable () -> Unit = {
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