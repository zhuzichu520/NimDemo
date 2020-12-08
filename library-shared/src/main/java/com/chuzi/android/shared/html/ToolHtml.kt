package com.chuzi.android.shared.html

import android.webkit.WebView
import com.chuzi.android.shared.skin.SkinManager

fun WebView.loadWithData(data: String) {
    this.loadDataWithBaseURL("file:///android_asset/html/", data, "text/html", "UTF-8", null)
}

fun WebView.showLoading() {
    this.loadWithData(generateLoadingHtml())
}

fun WebView.showCode(code: String, extension: String, wrap: Boolean = false) {
    this.loadWithData(generateCodeHtml(code, extension, wrap))
}

fun WebView.showMarkdown(markdown: String) {
    this.loadWithData(generateMarkdownHtml(markdown))
}

private fun generateLoadingHtml(): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
         <title>Loading...</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="./loading.css">
        </head>
        <body>
        <div class="loading">
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        <span></span>
        </div>
        </body>
        </html>
         """.trimIndent()
}

private fun generateCodeHtml(code: String, extension: String, wrap: Boolean): String {
    val wordWrap = if (wrap) "break-word" else "normal"
    val whiteSpace = if (wrap) "pre-wrap" else "no-wrap"
    return """
        <!DOCTYPE html>
        <html>
        <head>
        <title>Code Viewer</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
        pre.prettyprint {
        word-wrap:${wordWrap};
        white-space:${whiteSpace};
        }
        </style>
        <script src="./core/run_prettify.js?autoload=true&skin=${getCodeSkin()}&lang=${extension}&defer">
        </script>
        </head>
        <body>
        <?prettify lang=${extension} linenums=1?>
        <pre class="prettyprint">$code</pre>
        </body>
        </html>
        """.trimIndent()
}

private fun generateMarkdownHtml(markdown: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
        <title>Markdown Viewer</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link id="markdown" rel="stylesheet" type="text/css" href="${getMarkdownSkin()}">
        </head>
        <body>
        $markdown
        </body>
        </html>
        """.trimIndent()
}

private fun getCodeSkin(): String {
    return if (SkinManager.isDark())
        "sons-of-obsidian"
    else
        "prettify"
}

private fun getMarkdownSkin(): String {
    return if (SkinManager.isDark())
        "./markdown_dark.css"
    else
        "./markdown.css"
}