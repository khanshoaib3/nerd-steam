package com.github.khanshoaib3.steamcompanion.ui.screen.detail
// Ref: https://chatgpt.com/c/67dd4b9f-141c-8005-b477-0950c9a91681

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

// ENTRY POINT
@Composable
fun RenderHtmlContent(html: String, removeLineBreaks: Boolean = false, modifier: Modifier = Modifier) {
    var html2 = "<p>$html<p/>"
    if (removeLineBreaks) {
        html2 = html2.replace("<br/>", "")
        html2 = html2.replace("<br>", "")
    }
    val document = Jsoup.parse(html2).body()
    Column(modifier = modifier.padding(16.dp)) {
        document.children().forEach { element ->
            RenderElement(element)
        }
    }
}

// DISPATCH ELEMENTS
@Composable
private fun RenderElement(element: Element) {
    when (element.tagName()) {
        "p" -> if (element.text().isNotBlank()) RenderParagraph(element)
        "h2" -> RenderHeader(element)
        "img" -> RenderImage(element)
        "ul" -> RenderUnorderedList(element)
        else -> if (element.text().isNotBlank()) RenderParagraph(element)
    }
}

// COMPONENTS
@Composable
private fun RenderParagraph(element: Element) {
    Text(
        text = parseInlineContent(element),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

/*
@Composable
fun RenderParagraph(element: Element) {
    val inlineContent = remember { mutableMapOf<String, InlineTextContent>() }
    val annotatedString = buildAnnotatedString {
        var imgCounter = 0

        for (node in element.childNodes()) {
            when {
                node is TextNode -> append(node.text())
                node is Element && node.tagName() == "img" -> {
                    val key = "inlineImage_$imgCounter"
                    imgCounter++

                    // Insert placeholder in the text stream
                    appendInlineContent(key, "[img]")

                    // Register the inline content (actual image composable)
                    val src = node.attr("src")
                    val maxInlineImageSize = 120.dp
                    val density: Density = LocalDensity.current
                    val maxInlineImageSizeSp: TextUnit
                    with(density) {
                        maxInlineImageSizeSp = maxInlineImageSize.toSp()
                    }

                    inlineContent[key] = InlineTextContent(
                        placeholder = Placeholder(
                            width = maxInlineImageSizeSp,
                            height = maxInlineImageSizeSp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) {
                        AsyncImage(
                            model = src,
                            contentDescription = null,
                            modifier = Modifier.sizeIn(
                                maxWidth = maxInlineImageSize,
                                maxHeight = maxInlineImageSize
                            )
                        )
                    }
                }
                node is Element -> {
                    // Parse other inline tags like <b>, <i> normally
                    ParseNode(this, node)
                }
            }
        }
    }

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
*/


@Composable
private fun RenderHeader(element: Element) {
    Text(
        text = parseInlineContent(element),
        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
private fun RenderImage(element: Element) {
    val src = element.attr("src")
    AsyncImage(
        model = src,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(vertical = 8.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun RenderUnorderedList(element: Element) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        element.select("li").forEach { li ->
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(text = "• ", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = parseInlineContent(li),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

// INLINE PARSER
@Composable
private fun parseInlineContent(element: Element): AnnotatedString {
    return buildAnnotatedString {
        ParseNode(this, element)
    }
}

@Composable
private fun ParseNode(builder: AnnotatedString.Builder, node: Node) {
    when (node) {
        is TextNode -> builder.append(node.text())
        is Element -> {
            when (node.tagName()) {
                // Bold
                "b", "strong" -> builder.withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Italic
                "i", "em" -> builder.withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Underline
                "u" -> builder.withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Strikethrough
                "del", "s" -> builder.withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Mark / highlight
                "mark" -> builder.withStyle(
                    SpanStyle(
                        background = MaterialTheme.colorScheme.secondary.copy(
                            alpha = 0.3f
                        )
                    )
                ) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Smaller text
                "small" -> builder.withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    node.childNodes().forEach { ParseNode(this, it) }
                }
                // Line breaks
                "br" -> builder.append("\n")
                "a" -> node.childNodes()
                    .forEach { ParseNode(builder, it) } // (No link clicks for now)
                else -> node.childNodes().forEach { ParseNode(builder, it) }
            }
        }
    }
}
