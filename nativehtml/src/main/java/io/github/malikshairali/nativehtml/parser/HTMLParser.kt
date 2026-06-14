package io.github.malikshairali.nativehtml.parser

import androidx.compose.ui.text.TextStyle
import io.github.malikshairali.nativehtml.model.Blockquote
import io.github.malikshairali.nativehtml.model.Div
import io.github.malikshairali.nativehtml.model.HTMLElement
import io.github.malikshairali.nativehtml.model.HorizontalRule
import io.github.malikshairali.nativehtml.model.Image
import io.github.malikshairali.nativehtml.model.InlineCode
import io.github.malikshairali.nativehtml.model.LineBreak
import io.github.malikshairali.nativehtml.model.ListItem
import io.github.malikshairali.nativehtml.model.OrderedList
import io.github.malikshairali.nativehtml.model.Paragraph
import io.github.malikshairali.nativehtml.model.PreformattedText
import io.github.malikshairali.nativehtml.model.Span
import io.github.malikshairali.nativehtml.model.Table
import io.github.malikshairali.nativehtml.model.TableCell
import io.github.malikshairali.nativehtml.model.TableHeaderCell
import io.github.malikshairali.nativehtml.model.TableRow
import io.github.malikshairali.nativehtml.model.TextElement
import io.github.malikshairali.nativehtml.model.UnorderedList
import io.github.malikshairali.nativehtml.model.UnsupportedHtml
import io.github.malikshairali.nativehtml.style.CssParser
import io.github.malikshairali.nativehtml.NativeHTMLBuilder
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.TextNode

class HTMLParser(private val builder: NativeHTMLBuilder = NativeHTMLBuilder()) {
    fun parse(html: String): List<HTMLElement> {
        val document = Ksoup.parse(html)
        return document.body().children().flatMap { parseElement(it) }
    }

    private fun parseElement(
        element: Element,
        parentTextStyle: TextStyle = TextStyle()
    ): List<HTMLElement> {
        val tag = element.tagName()
        
        builder.customRenderers[tag]?.let { renderer ->
            return listOf(renderer(element))
        }

        val inlineCss = element.attr("style")
        val style = parentTextStyle.merge(getTextStyle(tag, inlineCss))

        return when (tag) {
            "h1" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "h2" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "h3" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "h4" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "h5" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "h6" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "small" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "u" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "s", "strike", "del" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "mark" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "sub" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "sup" -> listOf(
                TextElement(
                    text = element.text(),
                    style = style
                )
            )

            "b", "strong" -> listOf(
                Span(
                    parseChildren(
                        element = element,
                        style = style
                    )
                )
            )

            "i", "em" -> listOf(
                Span(
                    parseChildren(
                        element = element,
                        style = style
                    )
                )
            )

            "a" -> listOf(
                TextElement(
                    text = element.text(),
                    href = element.attr("href"),
                    style = style
                )
            )

            "blockquote" -> listOf(Blockquote(element.text()))

            "code" -> listOf(
                InlineCode(
                    text = element.text()
                )
            )

            "pre" -> listOf(
                PreformattedText(
                    text = element.wholeText(),
                    style = style
                )
            )

            "span" -> listOf(
                Span(
                    children = parseChildren(element, style),
                    style = style
                )
            )

            "p" -> listOf(
                Paragraph(
                    children = parseChildren(element, style),
                    style = style
                )
            )

            "br" -> listOf(LineBreak)

            "ul" -> listOf(UnorderedList(element.children().flatMap { parseElement(it) }))

            "ol" -> listOf(OrderedList(element.children().flatMap { parseElement(it) }))

            "li" -> {
                val children = parseChildren(element, style)
                listOf(ListItem(children))
            }

            "table" -> {
                val rows =
                    element.children().flatMap { parseElement(it) }.filterIsInstance<TableRow>()
                listOf(Table(rows))
            }

            "tbody" -> {
                element.children().flatMap { parseElement(it) }
            }

            "thead", "tfoot" -> {
                element.children().flatMap { parseElement(it) }
            }

            "tr" -> {
                val cells =
                    element.children().flatMap { parseElement(it) }.filter {
                        it is TableCell || it is TableHeaderCell
                    }
                listOf(TableRow(cells))
            }

            "td" -> {
                val children = parseChildren(element, style)
                listOf(TableCell(children))
            }

            "th" -> {
                val children = parseChildren(element, style)
                listOf(TableHeaderCell(children))
            }

            "img" -> listOf(Image(element.attr("src"), element.attr("alt")))

            "div", "section", "article", "header", "footer", "nav", "main" -> listOf(
                Div(
                    parseChildren(
                        element,
                        style
                    )
                )
            )

            "hr" -> listOf(HorizontalRule)

            else -> listOf(UnsupportedHtml(element.outerHtml())) // Unsupported tags
        }
    }

    private fun parseChildren(
        element: Element,
        style: TextStyle = TextStyle()
    ): List<HTMLElement> {
        val children = mutableListOf<HTMLElement>()

        // Iterate through all child nodes (text + elements)
        element.childNodes().forEach { node ->
            when (node) {
                is TextNode -> {
                    // Handle plain text nodes
                    if (node.text().isNotBlank()) {
                        children.add(TextElement(text = node.text(), style = style))
                    }
                }

                is Element -> {
                    // Recursively parse child elements
                    children.addAll(
                        parseElement(
                            element = node,
                            parentTextStyle = style
                        )
                    )
                }
            }
        }

        return children
    }

    private fun getTextStyle(tag: String, css: String?): TextStyle {
        val styleFromCss = CssParser.parse(css)
        val styleForTag = builder.styleRegistry.getStyle(tag)
        return styleFromCss.merge(styleForTag)
    }
}
