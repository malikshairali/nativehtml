Here's a well-structured **README.md** for your `NativeHTML` library:  

---

### **📜 NativeHTML - Convert HTML to Jetpack Compose UI**  

[![Maven Central](https://img.shields.io/maven-central/v/io.github.malikshairali/native-html)](https://central.sonatype.com/artifact/io.github.malikshairali/native-html)  
[![License](https://img.shields.io/badge/license-Apache%202.0-blue)](https://opensource.org/licenses/Apache-2.0)  
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Supported-brightgreen)](https://developer.android.com/jetpack/compose)  

**NativeHTML** is an Android library that converts raw **HTML** into **Jetpack Compose UI**.  
Easily render HTML tags as native **Composable UI components**, including **text, images, lists, tables, and more**.  

---

## **🚀 Features**
✅ **Convert HTML to Native Compose UI**  
✅ **Support for Common HTML Tags** (`<p>`, `<h1>`-`<h6>`, `<ul>`, `<ol>`, `<img>`, `<a>`, `<strong>`, `<em>`, etc.)  
✅ **Nested Lists, Tables, and Blockquotes**  
✅ **Hyperlinks with Click Support**  
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
    implementation("io.github.malikshairali:nativehtml:0.0.2")
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
### **🔹 Override Heading Styles**
```kotlin
HeadingStyleRegistry.setStyle(1, TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold))
```

---

## **📜 Supported HTML Tags**
| Tag | Description |
|---|---|
| `<p>` | Paragraph |
| `<h1>` - `<h6>` | Headings |
| `<strong>` / `<b>` | Bold text |
| `<em>` / `<i>` | Italic text |
| `<a>` | Hyperlink with click support |
| `<ul>`, `<ol>`, `<li>` | Lists |
| `<img>` | Image rendering |
| `<blockquote>` | Blockquote |
| `<code>` | Monospace text |
| `<sub>`, `<sup>` | Subscript & Superscript |
| `<table>`, `<tr>`, `<td>` | Table support |
| `<video>` | WebView-based video rendering |
| `<div>` | Block container |
| `<iframe>`, `<embed>` | WebView fallback rendering |

---

## **🔧 How It Works**
- **Parses HTML using Jsoup**
- **Maps HTML tags to Compose UI components**
- **Uses `LazyColumn` for efficient rendering**
- **Delegates unsupported elements (like `<video>`) to WebView**

---

## **📄 License**
```
Apache License 2.0
Copyright (c) 2024 Malik Shair Ali
```
[Read the full license](https://opensource.org/licenses/Apache-2.0).

---

## **🚀 Contribute**
**Pull requests are welcome!** Feel free to [open an issue](https://github.com/malikshairali/nativehtml/issues) for feature requests or bug reports.

---
