package io.github.malikshairali.nativehtml.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.sp

object StyleRegistry {
    private val customStyles: MutableMap<String, TextStyle> = mutableMapOf()

    fun setStyle(tag: String, style: TextStyle) {
        customStyles[tag] = style
    }

    fun getStyle(tag: String): TextStyle {
        return customStyles[tag] ?: defaultStyles(tag)
    }

    private fun defaultStyles(tag: String): TextStyle {
        return when (tag) {
            "h1" -> TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
            "h2" -> TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            "h3" -> TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium)
            "h4" -> TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)
            "h5" -> TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Light)
            "h6" -> TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Thin)
            "u" -> TextStyle(textDecoration = Underline)
            "b", "strong" -> TextStyle(fontWeight = FontWeight.Bold)
            "em" -> TextStyle(fontStyle = Italic)
            "sup" -> TextStyle(baselineShift = BaselineShift.Superscript)
            "sub" -> TextStyle(baselineShift = BaselineShift.Subscript)
            "mark" -> TextStyle(color = Color.Yellow)
            "a" -> TextStyle(color = Color.Blue, textDecoration = Underline)
            else -> TextStyle()
        }
    }
}
