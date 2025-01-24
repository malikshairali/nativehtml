package com.github.malikshairali.nativehtml

import com.github.malikshairali.nativehtml.model.HTMLElement
import com.github.malikshairali.nativehtml.model.HeadlineTagHandler
import org.jsoup.Jsoup

class HTMLParser() {
    fun parse(html: String): List<HTMLElement> {
        val document = Jsoup.parse(html)
        val elements = document.body().children()

        //
        TagHandlerRegistry.registerHandler(HeadlineTagHandler())
        //

        val htmlElements = mutableListOf<HTMLElement>()
        elements.forEach { element ->
            TagHandlerRegistry.getHandler(element.tagName())?.let {
                htmlElements.add(it.handle(element))
            }
        }

        return htmlElements
    }
}