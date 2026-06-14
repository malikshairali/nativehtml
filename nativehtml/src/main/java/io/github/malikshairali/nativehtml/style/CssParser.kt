package io.github.malikshairali.nativehtml.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

internal object CssParser {
    fun parse(css: String?): TextStyle {
        if (css.isNullOrBlank()) return TextStyle()

        val cssMap = css.split(";").mapNotNull {
            val (key, value) = it.split(":").map(String::trim).takeIf { it.size == 2 }
                ?: return@mapNotNull null
            key to value
        }

        return css {
            cssMap.forEach { (key, value) ->
                when (key.lowercase()) {
                    "color" -> color(value)
                    "font-size" -> fontSize(value)
                    "font-weight" -> fontWeight(value)
                    "font-style" -> fontStyle(value)
                    "text-align" -> textAlign(value)
                    "background-color" -> background(value)
                    "background" -> background(value)
                    "text-decoration" -> textDecoration(value)
                    "line-height" -> lineHeight(value)
                    "font-family" -> fontFamily(value)
                }
            }
        }
    }

    fun parseFontWeight(value: String): FontWeight? = when (value.trim()) {
        "normal" -> FontWeight.Normal
        "bold" -> FontWeight.Bold
        "100" -> FontWeight.W100
        "200" -> FontWeight.W200
        "300" -> FontWeight.W300
        "400" -> FontWeight.W400
        "500" -> FontWeight.W500
        "600" -> FontWeight.W600
        "700" -> FontWeight.W700
        "800" -> FontWeight.W800
        "900" -> FontWeight.W900
        else -> null
    }

    fun parseTextUnit(value: String): TextUnit {
        return when {
            value.endsWith("px") -> value.removeSuffix("px").toFloatOrNull()?.sp

            value.endsWith("em") -> value.removeSuffix("em").toFloatOrNull()?.em

            else -> value.toFloatOrNull()?.sp
        } ?: TextUnit.Unspecified
    }

    fun parseTextAlign(value: String): TextAlign = when (value.trim().lowercase()) {
        "left" -> TextAlign.Left
        "center" -> TextAlign.Center
        "right" -> TextAlign.Right
        "justify" -> TextAlign.Justify
        else -> TextAlign.Unspecified
    }

    fun parseTextDecoration(value: String): TextDecoration? {
        val normalized = value.trim().lowercase()
        if (normalized == "none") return null
        val parts = normalized.split(Regex("\\s+")).filter { it.isNotBlank() }
        var decoration: TextDecoration? = null

        parts.forEach { part ->
            when (part) {
                "underline" -> {
                    decoration = if (decoration == null) {
                        TextDecoration.Underline
                    } else {
                        decoration!! + TextDecoration.Underline
                    }
                }
                "line-through" -> {
                    decoration = if (decoration == null) {
                        TextDecoration.LineThrough
                    } else {
                        decoration!! + TextDecoration.LineThrough
                    }
                }
            }
        }
        return decoration
    }

    fun parseFontFamily(value: String): FontFamily? = when (value.trim().lowercase()) {
        "monospace" -> FontFamily.Monospace
        "sans-serif" -> FontFamily.SansSerif
        "serif" -> FontFamily.Serif
        else -> null
    }

    fun parseColor(value: String): Color {
        val trimmed = value.trim().lowercase()
        return cssColorMap[trimmed]
            ?: parseRgbColor(trimmed)
            ?: parseHexColor(trimmed)
            ?: Color.Unspecified
    }

    private fun parseHexColor(value: String): Color? {
        val hex = value.removePrefix("#")
        return try {
            when (hex.length) {
                3 -> {
                    val expanded = hex.map { "$it$it" }.joinToString("")
                    Color(expanded.toLong(16) or 0xFF000000)
                }
                4 -> {
                    val expanded = hex.map { "$it$it" }.joinToString("")
                    Color(expanded.toLong(16))
                }
                6 -> Color(hex.toLong(16) or 0xFF000000)
                8 -> Color(hex.toLong(16))
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseRgbColor(value: String): Color? {
        val rgbRegex = Regex("""rgb\(\s*(\d+),\s*(\d+),\s*(\d+)\s*\)""")
        rgbRegex.matchEntire(value)?.destructured?.let { (r, g, b) ->
            return Color(r.toInt(), g.toInt(), b.toInt())
        }

        val rgbaRegex = Regex("""rgba\(\s*(\d+),\s*(\d+),\s*(\d+),\s*([\d.]+%?)\s*\)""")
        rgbaRegex.matchEntire(value)?.destructured?.let { (r, g, b, a) ->
            val alpha = if (a.endsWith("%")) {
                ((a.removeSuffix("%").toFloatOrNull() ?: return null) / 100f)
            } else {
                a.toFloatOrNull() ?: return null
            }
            return Color(
                red = r.toInt(),
                green = g.toInt(),
                blue = b.toInt(),
                alpha = (alpha.coerceIn(0f, 1f) * 255).roundToInt()
            )
        }

        return null
    }

    private val cssColorMap = mapOf(
        "black" to Color.Black,
        "white" to Color.White,
        "red" to Color.Red,
        "blue" to Color.Blue,
        "green" to Color.Green,
        "gray" to Color.Gray,
        "lightgray" to Color.LightGray,
        "darkgray" to Color.DarkGray,
        "transparent" to Color.Transparent,
        "yellow" to Color.Yellow,
        "orange" to Color(0xFFFFA500),
        "purple" to Color(0xFF800080),
        "brown" to Color(0xFFA52A2A),
        "teal" to Color(0xFF008080),
        "navy" to Color(0xFF000080),
        "magenta" to Color.Magenta,
        "cyan" to Color.Cyan
    )
}