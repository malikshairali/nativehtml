package io.github.malikshairali.nativehtml.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit

class CssStyleBuilder {

    private var color: Color = Color.Unspecified
    private var fontSize: TextUnit = TextUnit.Unspecified
    private var fontWeight: FontWeight? = null
    private var fontStyle: FontStyle? = null
    private var textAlign: TextAlign = TextAlign.Unspecified
    private var background: Color = Color.Unspecified
    private var textDecoration: TextDecoration? = null
    private var lineHeight: TextUnit = TextUnit.Unspecified
    private var fontFamily: FontFamily? = null

    fun color(value: String) {
        this.color = CssParser.parseColor(value)
    }

    fun fontSize(value: String) {
        this.fontSize = CssParser.parseTextUnit(value)
    }

    fun fontWeight(value: String) {
        this.fontWeight = CssParser.parseFontWeight(value)
    }

    fun fontStyle(value: String) {
        this.fontStyle = when (value.trim().lowercase()) {
            "italic" -> FontStyle.Italic
            else -> FontStyle.Normal
        }
    }

    fun textAlign(value: String) {
        this.textAlign = CssParser.parseTextAlign(value)
    }

    fun background(value: String) {
        this.background = CssParser.parseColor(value)
    }

    fun textDecoration(value: String) {
        this.textDecoration = CssParser.parseTextDecoration(value)
    }

    fun lineHeight(value: String) {
        this.lineHeight = CssParser.parseTextUnit(value)
    }

    fun fontFamily(value: String) {
        this.fontFamily = CssParser.parseFontFamily(value)
    }

    fun toTextStyle(): TextStyle = TextStyle(
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        textAlign = textAlign,
        background = background,
        textDecoration = textDecoration,
        lineHeight = lineHeight,
        fontFamily = fontFamily
    )
}

fun css(build: CssStyleBuilder.() -> Unit): TextStyle {
    val builder = CssStyleBuilder()
    builder.build()
    return builder.toTextStyle()
}