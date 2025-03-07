package com.github.malikshairali.nativehtmldemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.malikshairali.nativehtml.RenderHtml

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RenderHtml(
                html = """
                        <h1>Title</h1>
                        <p>Here is H<sub>2</sub>O and some other text.</p>
                        <p>This is a <strong>bold</strong> text and <em>italic</em> text. I am going to make it long enough
                        to go over to the next line</p>
                        <p>This is a <strong>bold</strong> text with a <a href="https://example.com">hyperlink</a> and <em>italicized</em> text.</p>
                        <ul>
                            <li>Item 1</li>
                            <li>Item 2</li>
                        </ul>
                        <blockquote>This is a quote.</blockquote>
                        <a href="https://example.com">Click here</a>
                        <br />
                        <img src="https://t4.ftcdn.net/jpg/01/43/42/83/360_F_143428338_gcxw3Jcd0tJpkvvb53pfEztwtU9sxsgT.jpg" alt="Sample Image" />
                        <br />
                        
                        <ul>
                            <li>Item 1</li>
                            <li>Item 2 with <strong>bold</strong> text</li>
                            <li>
                                Nested list:
                                <ul>
                                    <li>Nested Item 1</li>
                                    <li>Nested Item 2</li>
                                </ul>
                            </li>
                        </ul>
                        
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
                                <td><mark>Marked Cell</mark></td>
                                <td><sub>Subscript</sub> and <sup>Superscript</sup></td>
                            </tr>
                        </table>
                        <p>Here is <span>some inline text</span>.</p>
                        <table>
                            <thead>
                                <tr>
                                    <td><strong>Header 1</strong></td>
                                    <td><strong>Header 2</strong></td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>Cell 1</td>
                                    <td>Cell 2</td>
                                </tr>
                                <tr>
                                    <td><mark>Marked Cell</mark></td>
                                    <td><sub>Subscript</sub> and <sup>Superscript</sup></td>
                                </tr>
                            </tbody>
                        </table>
                    """.trimIndent()
            )
        }
    }
}