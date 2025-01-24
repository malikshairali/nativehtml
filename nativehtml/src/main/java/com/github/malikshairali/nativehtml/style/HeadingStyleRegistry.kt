package com.github.malikshairali.nativehtml.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object HeadingStyleRegistry {
    private val customStyles: MutableMap<Int, TextStyle> = mutableMapOf()

    // Set custom style for a specific heading level
    fun setStyle(level: Int, style: TextStyle) {
        customStyles[level] = style
    }

    // Get the custom style if available, otherwise return default
    fun getStyle(level: Int): TextStyle {
        return customStyles[level] ?: defaultHeadingStyle(level)
    }

    // Default styles for each heading level
    private fun defaultHeadingStyle(level: Int): TextStyle {
        return when (level) {
            1 -> TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold)
            2 -> TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            3 -> TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium)
            4 -> TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)
            5 -> TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Light)
            6 -> TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Thin)
            else -> TextStyle.Default
        }
    }
}
