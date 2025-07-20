package io.github.malikshairali.nativehtml.parser

import androidx.compose.ui.text.TextStyle
import io.github.malikshairali.nativehtml.model.Blockquote
import io.github.malikshairali.nativehtml.model.Div
import io.github.malikshairali.nativehtml.model.HTMLElement
import io.github.malikshairali.nativehtml.model.Image
import io.github.malikshairali.nativehtml.model.InlineCode
import io.github.malikshairali.nativehtml.model.LineBreak
import io.github.malikshairali.nativehtml.model.ListItem
import io.github.malikshairali.nativehtml.model.OrderedList
import io.github.malikshairali.nativehtml.model.Paragraph
import io.github.malikshairali.nativehtml.model.Span
import io.github.malikshairali.nativehtml.model.Table
import io.github.malikshairali.nativehtml.model.TableCell
import io.github.malikshairali.nativehtml.model.TableRow
import io.github.malikshairali.nativehtml.model.TextElement
import io.github.malikshairali.nativehtml.model.UnorderedList
import io.github.malikshairali.nativehtml.model.UnsupportedHtml
import io.github.malikshairali.nativehtml.style.CssParser
import io.github.malikshairali.nativehtml.style.StyleRegistry
import org.jsoup.Jsoup

class HTMLParser {
    fun parse(html: String): List<HTMLElement> {
        val document = Jsoup.parse(html)
        return document.body().children().flatMap { parseElement(it) }
    }

    private fun parseElement(
        element: org.jsoup.nodes.Element,
        parentTextStyle: TextStyle = TextStyle()
    ): List<HTMLElement> {
        val tag = element.tagName()
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

            "u" -> listOf(
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

            "strong" -> listOf(
                Span(
                    parseChildren(
                        element = element,
                        style = style
                    )
                )
            )

            "em" -> listOf(
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
                val children = parseChildren(element)
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

            "tr" -> {
                val cells =
                    element.children().flatMap { parseElement(it) }.filterIsInstance<TableCell>()
                listOf(TableRow(cells))
            }

            "td" -> {
                val children = parseChildren(element)
                listOf(TableCell(children))
            }

            "img" -> listOf(Image(element.attr("src"), element.attr("alt")))

            "div" -> listOf(Div(parseChildren(element, style)))

            else -> listOf(UnsupportedHtml(element.outerHtml())) // Unsupported tags
        }
    }

    private fun parseChildren(
        element: org.jsoup.nodes.Element,
        style: TextStyle = TextStyle()
    ): List<HTMLElement> {
        val children = mutableListOf<HTMLElement>()

        // Iterate through all child nodes (text + elements)
        element.childNodes().forEach { node ->
            when (node) {
                is org.jsoup.nodes.TextNode -> {
                    // Handle plain text nodes
                    if (node.text().isNotBlank()) {
                        children.add(TextElement(text = node.text(), style = style))
                    }
                }

                is org.jsoup.nodes.Element -> {
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

    private fun getTextStyle(tag: String, css: String?) : TextStyle {
        val styleFromCss = CssParser.parse(css)
        val styleForTag = StyleRegistry.getStyle(tag)
        return styleFromCss.merge(styleForTag)
    }
}
