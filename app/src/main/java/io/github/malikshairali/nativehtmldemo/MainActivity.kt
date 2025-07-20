package io.github.malikshairali.nativehtmldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.malikshairali.nativehtml.RenderHtml
import io.github.malikshairali.nativehtml.style.StyleRegistry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Override default styles
        StyleRegistry.setStyle("h1", TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold))

        setContent {
            RenderHtml(
                html = """
                    <h1 style="color: red; font-size: 36px;">Heading 1</h1>
                    <h2 style="color: green;">Heading 2</h2>
                    <p style="font-style: italic; font-weight: 400; font-size: 18px; line-height: 1.5em; text-align: justify; background-color: #f0f0f0;">
                        This is a long paragraph, justify aligned, with <u>underlined</u>, <mark>highlighted</mark>, <sub>subscript</sub>, and <sup>superscript</sup> text.
                    </p>
                    <p style="text-align: center;"><strong style="color: blue; text-align: center;">Blue Bold Center Aligned</strong></p>
                    <p style="text-align: right;"><em style="font-family: monospace; text-align: right">This is Italic and right aligned</em></p>
                    <a href="https://example.com" style="color: magenta; text-decoration: underline;">A link you can click on</a>,
                    <code>Some Inline Code</code>,
                    
                    <blockquote>This is a blockquote.</blockquote>
                    
                    <p>Unordered list:</p>
                    <ul>
                        <li>Item 1</li>
                        <li>Item 2 with <strong>bold</strong> text</li>
                        <li>Nested list:
                            <ul>
                                <li>Nested Item 1</li>
                                <li>Nested Item 2</li>
                            </ul>
                        </li>
                    </ul>
                    
                    <p>Ordered list:</p>
                    <ol>
                        <li>Eat</li>
                        <li>Sleep</li>
                        <li>Repeat</li>
                    </ol>
                    
                    <p>Here is a table:</p>
                    <table>
                        <tr>
                            <td><strong>Header 1</strong></td>
                            <td><strong>Header 2</strong></td>
                        </tr>
                        <tr>
                            <td>Cell 1</td>
                            <td>Cell 2</td>
                        </tr>
                        <tr>
                            <td>Row 2, Col 1 with <em>italic</em> text.</td>
                            <td><sub>Subscript</sub> and <sup>Superscript</sup></td>
                        </tr>
                        <tr>
                            <td>
                                <p>Nested paragraph inside a cell.</p>
                            </td>
                            <td>
                                <ul>
                                    <li>Nested list item 1</li>
                                    <li>Nested list item 2</li>
                                </ul>
                            </td>
                        </tr>
                    </table>
                    
                    <p>Here is an image:</p>
                    <img src="https://fastly.picsum.photos/id/7/4728/3168.jpg?hmac=c5B5tfYFM9blHHMhuu4UKmhnbZoJqrzNOP9xjkV4w3o" alt="Image" />
                    
                    <p>Here is a video:</p>
                    <iframe src="https://www.youtube.com/embed/QSOUdL30z7E" controls autoplay></iframe>
                    
                    <div style="background-color: lightblue;">
                        <p>Text inside div</p>
                        <ul>
                            <li>Nested in div</li>
                        </ul>
                    </div>
                    
                    <p>Mixed inline: <strong>bold</strong>, <em>italic</em>, <a href="https://example.com">link</a>, <sub>sub</sub>, <sup>sup</sup>.</p>
                    """.trimIndent()
            )
        }
    }
}