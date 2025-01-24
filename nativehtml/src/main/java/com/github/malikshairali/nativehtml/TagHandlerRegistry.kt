package com.github.malikshairali.nativehtml

import com.github.malikshairali.nativehtml.model.HTMLElement
import org.jsoup.nodes.Element


interface HtmlTagHandler {
    fun handle(element: Element): HTMLElement
    fun canHandle(tagName: String): Boolean
}


object TagHandlerRegistry {
    private val handlers = mutableListOf<HtmlTagHandler>()

    fun registerHandler(handler: HtmlTagHandler) {
        handlers.add(handler)
    }

    fun getHandler(tagName: String): HtmlTagHandler? {
        return handlers.find { it.canHandle(tagName) }
    }
}