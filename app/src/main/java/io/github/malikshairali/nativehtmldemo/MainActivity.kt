package io.github.malikshairali.nativehtmldemo

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.github.malikshairali.nativehtml.NativeHTML
import io.github.malikshairali.nativehtml.model.TextElement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dummyHtmlString = """
                <!-- 1. Headings (h1 to h6) with CSS colors and sizes -->
                <div style="background-color: #f5f5f5; padding: 16px;">
                    <h1 style="color: red; text-align: center; font-family: sans-serif;">Heading 1 - Red &amp; Centered</h1>
                    <h2 style="color: #00FF00; text-align: right; text-decoration: underline;">Heading 2 - Green, Right, Underlined</h2>
                    <h3 style="color: rgb(0, 0, 255); font-weight: 300;">Heading 3 - Blue RGB &amp; Light Weight</h3>
                    <h4 style="color: rgba(255, 165, 0, 0.8); font-style: italic;">Heading 4 - Orange RGBA &amp; Italic</h4>
                    <h5 style="font-family: monospace; font-size: 1.2em;">Heading 5 - Monospace &amp; 1.2em size</h5>
                    <h6 style="color: magenta; line-height: 2em;">Heading 6 - Magenta &amp; 2em Line Height</h6>
                </div>
                
                <br>
                
                <!-- 2. Paragraphs & Inline Text Formatting -->
                <p style="text-align: justify; font-size: 16px; font-family: serif;">
                    This is a justified paragraph containing various inline styles. Here is some <strong>strong text</strong> and some <b>bold text</b>. 
                    You can also see <em>emphasized (italic) text</em>. If you need to <u>underline text</u> or use a <span style="text-decoration: line-through;">strikethrough span</span>, you can do that too. 
                    Here is a <mark>highlighted (marked) word</mark>. We also support chemical formulas like H<sub>2</sub>O and equations like E=mc<sup>2</sup>.
                </p>
                
                <!-- 3. Links and Inline Code -->
                <p style="text-align: center;">
                    Check out this <a href="https://example.com" style="color: cyan; font-weight: bold;">custom styled link</a>. 
                    Also, here is some inline code: <code style="background-color: black; color: white;">val x = 10</code>.
                </p>
                
                <!-- 4. Blockquotes -->
                <blockquote style="background-color: #e9ecef; font-style: italic; color: #555;">
                    "This is a blockquote demonstrating how quotes are rendered with a leading accent line."
                </blockquote>
                
                <br>
                
                <!-- 5. Lists (Ordered, Unordered, and Nested) -->
                <div style="background-color: #e0f7fa;">
                    <p style="font-weight: 700;">Unordered List:</p>
                    <ul>
                        <li>First item</li>
                        <li>Second item with a <a href="https://google.com">link</a></li>
                        <li>
                            Nested Ordered List:
                            <ol>
                                <li style="color: red;">Nested 1 (Red)</li>
                                <li style="color: blue;">Nested 2 (Blue)</li>
                            </ol>
                        </li>
                    </ul>
                </div>
                
                <br>
                
                <!-- 6. Tables -->
                <div style="background-color: #fff9c4;">
                    <p style="font-weight: bold; text-align: center;">Data Table:</p>
                    <table style="text-align: center;">
                        <tbody>
                            <tr>
                                <td style="background-color: #b2ebf2; font-weight: bold;">Header A</td>
                                <td style="background-color: #b2ebf2; font-weight: bold;">Header B</td>
                            </tr>
                            <tr>
                                <td>Row 1, Cell 1</td>
                                <td style="font-style: italic;">Row 1, Cell 2</td>
                            </tr>
                            <tr>
                                <td>Row 2, Cell 1 <br> (With Line Break)</td>
                                <td><mark>Marked Cell</mark></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <br>
                
                <!-- 7. Images -->
                <p style="text-align: center; font-weight: bold;">Sample Image:</p>
                <img src="https://fastly.picsum.photos/id/7/4728/3168.jpg?hmac=c5B5tfYFM9blHHMhuu4UKmhnbZoJqrzNOP9xjkV4w3o" alt="Sample image" />
                
                <br>
                
                <!-- 8. Unsupported HTML (Fallback to WebView) -->
                <p style="font-weight: bold; color: red;">Unsupported Tag Fallback (WebView Frame):</p>
                <iframe width="100%" height="200" src="https://www.youtube.com/embed/dQw4w9WgXcQ" frameborder="0" allowfullscreen></iframe>
            """.trimIndent()

            NativeHTML(
                html = dummyHtmlString,
                onLinkClick = { url ->
                    val uri = Uri.parse(url)
                    val isAppDeepLink =
                        uri.scheme == "nativehtml" || (uri.host == "example.com" && uri.path?.startsWith("/app/") == true)
                    isAppDeepLink // return true to consume, false to let NativeHTML open browser
                }
            ) {
                style("h1") {
                    color("black")
                    fontSize("40sp")
                }
                customTag("blockquote") { element ->
                    TextElement(
                        text = "CUSTOM BLOCKQUOTE: " + element.text(),
                        style = TextStyle(color = Color.Magenta)
                    )
                }
            }
        }
    }
}
