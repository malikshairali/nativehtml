package com.github.malikshairali.nativehtml

import com.github.malikshairali.nativehtml.model.Anchor
import com.github.malikshairali.nativehtml.model.Blockquote
import com.github.malikshairali.nativehtml.model.BoldText
import com.github.malikshairali.nativehtml.model.EmphasizedText
import com.github.malikshairali.nativehtml.model.HTMLElement
import com.github.malikshairali.nativehtml.model.Heading
import com.github.malikshairali.nativehtml.model.Image
import com.github.malikshairali.nativehtml.model.InlineCode
import com.github.malikshairali.nativehtml.model.LineBreak
import com.github.malikshairali.nativehtml.model.ListItem
import com.github.malikshairali.nativehtml.model.Mark
import com.github.malikshairali.nativehtml.model.OrderedList
import com.github.malikshairali.nativehtml.model.Paragraph
import com.github.malikshairali.nativehtml.model.Span
import com.github.malikshairali.nativehtml.model.Subscript
import com.github.malikshairali.nativehtml.model.Superscript
import com.github.malikshairali.nativehtml.model.Table
import com.github.malikshairali.nativehtml.model.TableCell
import com.github.malikshairali.nativehtml.model.TableRow
import com.github.malikshairali.nativehtml.model.TextNode
import com.github.malikshairali.nativehtml.model.UnorderedList
import org.jsoup.Jsoup

class HTMLParser {
    fun parse(html: String): List<HTMLElement> {
        val document = Jsoup.parse(html)
        return document.body().children().flatMap { parseElement(it) }
    }

    private fun parseElement(element: org.jsoup.nodes.Element): List<HTMLElement> {
        return when (element.tagName()) {
            "h1" -> listOf(Heading(1, element.text()))
            "h2" -> listOf(Heading(2, element.text()))
            "h3" -> listOf(Heading(3, element.text()))
            "h4" -> listOf(Heading(4, element.text()))
            "h5" -> listOf(Heading(5, element.text()))
            "h6" -> listOf(Heading(6, element.text()))
            "p" -> listOf(Paragraph(parseChildren(element)))
            "strong", "b" -> listOf(BoldText(element.text()))
            "a" -> listOf(Anchor(element.attr("href"), element.text()))
            "code" -> listOf(InlineCode(element.text()))
            "em" -> listOf(EmphasizedText(element.text()))
            "blockquote" -> listOf(Blockquote(element.text()))
            "br" -> listOf(LineBreak)
            "ul" -> listOf(UnorderedList(element.children().flatMap { parseElement(it) }))
            "ol" -> listOf(OrderedList(element.children().flatMap { parseElement(it) }))
            "img" -> listOf(Image(element.attr("src"), element.attr("alt")))
            "li" -> {
                // Process the content of <li> (text + children)
                val children = parseChildren(element)
                listOf(ListItem(children))
            }
            "table" -> {
                // Parse rows inside <table> (direct or through <tbody>)
                val rows = element.children().flatMap { parseElement(it) }.filterIsInstance<TableRow>()
                listOf(Table(rows))
            }
            "tbody" -> {
                // Pass rows from <tbody> to <table>
                element.children().flatMap { parseElement(it) }
            }
            "tr" -> {
                // Parse cells inside <tr>
                val cells = element.children().flatMap { parseElement(it) }.filterIsInstance<TableCell>()
                listOf(TableRow(cells))
            }
            "td" -> {
                // Parse content inside <td>
                val children = parseChildren(element)
                listOf(TableCell(children))
            }
            "span" -> listOf(Span(parseChildren(element)))
            "mark" -> listOf(Mark(element.text()))
            "sub" -> listOf(Subscript(element.text()))
            "sup" -> listOf(Superscript(element.text()))
            else -> emptyList() // Unsupported tags
        }
    }

    private fun parseChildren(element: org.jsoup.nodes.Element): List<HTMLElement> {
        val children = mutableListOf<HTMLElement>()

        // Iterate through all child nodes (text + elements)
        element.childNodes().forEach { node ->
            when (node) {
                is org.jsoup.nodes.TextNode -> {
                    // Handle plain text nodes
                    if (node.text().isNotBlank()) {
                        children.add(TextNode(node.text()))
                    }
                }
                is org.jsoup.nodes.Element -> {
                    // Recursively parse child elements
                    children.addAll(parseElement(node))
                }
            }
        }

        return children
    }
}
