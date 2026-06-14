### **📜 NativeHTML - Convert HTML and CSS to Jetpack Compose UI**  

[![Maven Central](https://img.shields.io/maven-central/v/io.github.malikshairali/native-html)](https://central.sonatype.com/artifact/io.github.malikshairali/native-html/1.0.0)  
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](https://opensource.org/licenses/Apache-2.0)  
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Supported-brightgreen)](https://developer.android.com/jetpack/compose)  

**NativeHTML** is an Android library that converts raw **HTML** and **CSS** into **Jetpack Compose UI**.  
Easily render HTML tags as native **Composable UI components**, including **text, images, lists, tables, and more**.  

---

## **🚀 Features**
✅ **Convert HTML to Native Compose UI**  
✅ **Support for Common HTML Tags** (`<p>`, `<h1>`-`<h6>`, `<ul>`, `<ol>`, `<img>`, `<a>`, `<strong>`, `<em>`, `<small>`, `<pre>`, `<hr>`, `<th>`, etc.)  
✅ **Support for inline CSS styles** (e.g., `style="color: red; font-weight: bold; text-align: center;"`)   
✅ **Style customization via StyleRegistry**   
✅ **Nested Lists, Tables, and Blockquotes**    
✅ **Hyperlinks with Click Support** (customizable with deep-link-first handling)  
✅ **WebView for Unsupported Elements** (`<video>`, `<iframe>`)  

---

## **📦 Installation**

### **1️⃣ Add Dependency**
Make sure `mavenCentral()` is included in your `repositories`:

```kotlin
repositories {
    google()
    mavenCentral()
}
```

Then, add the dependency in your **app module**:

```kotlin
dependencies {
    implementation("io.github.malikshairali:native-html:1.0.0")
}
```

---

## **🛠️ Usage**
### **🔹 Render Basic HTML**
```kotlin
RenderHtml(
    html = "<p>Hello, <strong>world</strong>! Visit <a href='https://example.com'>this link</a>.</p>"
)
```

### **🔹 Deep-Link-First URL Handling**
```kotlin
RenderHtml(
    html = "<p>Open <a href='myapp://profile/42'>profile</a> or <a href='https://example.com'>website</a></p>",
    onLinkClick = { url ->
        val uri = Uri.parse(url)
        val handledInApp = uri.scheme == "myapp"

        if (handledInApp) {
            // Route inside your app nav graph / deep-link router.
            true
        } else {
            false // NativeHTML falls back to ACTION_VIEW.
        }
    }
)
```

### **🔹 Render Lists**
```kotlin
RenderHtml(
    html = """
        <ul>
            <li>Item 1</li>
            <li><strong>Bold Item 2</strong></li>
            <li><em>Italic Item 3</em></li>
        </ul>
    """
)
```

### **🔹 Render Images**
```kotlin
RenderHtml(
    html = """<img src="https://via.placeholder.com/150" alt="Sample Image" />"""
)
```

### **🔹 Render Tables**
```kotlin
RenderHtml(
    html = """
        <table>
            <tr><td><strong>Header 1</strong></td><td><strong>Header 2</strong></td></tr>
            <tr><td>Row 1, Col 1</td><td>Row 1, Col 2</td></tr>
        </table>
    """
)
```

### **🔹 Render Video (Handled by WebView)**
```kotlin
RenderHtml(
    html = """<video src="https://www.youtube.com/embed/dQw4w9WgXcQ" controls autoplay></video>"""
)
```

---

## **🎨 Customization**
### **🔹 Override Tags Styles**

You can override the default styles using `StyleRegistry.setStyle(tag, textStyle)`:
```kotlin
StyleRegistry.setStyle("h1", TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold))
```

This allows full control over text appearance of elements like headings, italics, bold, links, and more.

---

## **📜 Supported HTML Tags**
| Tag | Description |
|---|---|
| `<p>` | Paragraph |
| `<h1>` - `<h6>` | Headings |
| `<strong>` / `<b>` | Bold text |
| `<em>` / `<i>` | Italic text |
| `<s>` / `<strike>` / `<del>` | Strikethrough text |
| `<small>` | Smaller helper text |
| `<a>` | Hyperlink with click support |
| `<ul>`, `<ol>`, `<li>` | Lists |
| `<img>` | Image rendering |
| `<blockquote>` | Blockquote |
| `<code>` | Monospace text |
| `<pre>` | Preformatted multiline text |
| `<sub>`, `<sup>` | Subscript & Superscript |
| `<table>`, `<tr>`, `<th>`, `<td>` | Table support |
| `<hr>` | Horizontal divider |
| `<video>` | WebView-based video rendering |
| `<div>`, `<section>`, `<article>`, `<header>`, `<footer>`, `<nav>`, `<main>` | Block container |
| `<iframe>`, `<embed>` | WebView fallback rendering |

---

## **🎨 Supported CSS Styles**
| CSS Property        | Description                            |
|---------------------|----------------------------------------|
| `color`             | Sets the text color                    |
| `background-color`  | Sets the background color              |
| `background`        | Sets the background color              |
| `font-weight`       | Controls boldness (`normal`, `bold`, `100`–`900`) |
| `font-style`        | Italic style (`normal`, `italic`)      |
| `font-size`         | Sets the font size (e.g., `16px`, `1.2em`) |
| `font-family`       | Applies the font family                |
| `text-align`        | Aligns text (`left`, `center`, `right`) |
| `text-decoration`   | Underlines/strikes text (`underline`, `line-through`, combined values) |

---

## **🔧 How It Works**
- **Parses HTML using Ksoup**
- **Maps HTML tags to Compose UI components**
- **Map CSS styling for each HTML tag to TextStyle and passes on to individual render** 
- **Uses `LazyColumn` for efficient rendering**
- **Delegates unsupported elements (like `<video>`) to WebView**

---

## **📄 License**
```
Apache License 2.0
Copyright (c) 2025 Malik Shair Ali
```
[Read the full license](https://opensource.org/licenses/Apache-2.0).

---

## **🚀 Contribute**
**Pull requests are welcome!** Feel free to [open an issue](https://github.com/malikshairali/nativehtml/issues) for feature requests or bug reports.

---
