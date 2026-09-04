package com.mataku.scrobscrob.ui_common.extension

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

private val hrefPattern = Regex("""href\s*=\s*["']([^"']*)["']""", RegexOption.IGNORE_CASE)
private val namedEntities = mapOf(
  "amp" to "&",
  "lt" to "<",
  "gt" to ">",
  "quot" to "\"",
  "apos" to "'",
  "nbsp" to " ",
)

private class OpenTag(val name: String, val start: Int, val href: String? = null)

private class ParserState {
  val open = ArrayDeque<OpenTag>()
  var lastChar: Char? = null
}

fun String.htmlToAnnotatedString(linkStyle: SpanStyle): AnnotatedString = buildAnnotatedString {
  val html = this@htmlToAnnotatedString
  val state = ParserState()
  var index = 0
  while (index < html.length) {
    val char = html[index]
    when {
      char == '<' && isTagStart(html, index) -> {
        val close = html.indexOf('>', index)
        if (close == -1) {
          append(html.substring(index + 1))
          break
        }
        handleTag(html.substring(index + 1, close).trim(), state, linkStyle)
        index = close + 1
      }
      char == '&' -> {
        val semicolon = html.indexOf(';', index)
        val decoded = if (semicolon in index + 2..index + 8) decodeEntity(html.substring(index + 1, semicolon)) else null
        if (decoded == null) {
          append('&')
          state.lastChar = '&'
          index++
        } else {
          append(decoded)
          state.lastChar = decoded.lastOrNull()
          index = semicolon + 1
        }
      }
      char == ' ' || char == '\n' || char == '\t' || char == '\r' -> {
        if (state.lastChar != null && state.lastChar != '\n' && state.lastChar != ' ') {
          append(' ')
          state.lastChar = ' '
        }
        index++
      }
      else -> {
        append(char)
        state.lastChar = char
        index++
      }
    }
  }
}

private fun isTagStart(html: String, index: Int): Boolean {
  val next = html.getOrNull(index + 1) ?: return false
  return next.isLetter() || next == '/'
}

private fun AnnotatedString.Builder.handleTag(raw: String, state: ParserState, linkStyle: SpanStyle) {
  val closing = raw.startsWith("/")
  val body = raw.removePrefix("/").removeSuffix("/").trim()
  val name = body.takeWhile { !it.isWhitespace() }.lowercase()
  when (name) {
    "br" -> {
      append('\n')
      state.lastChar = '\n'
    }
    "p" -> if (closing || length > 0) {
      append('\n')
      state.lastChar = '\n'
    }
    "b", "strong", "i", "em", "a" -> {
      if (closing) {
        val tag = state.open.lastOrNull { it.name == name } ?: return
        state.open.remove(tag)
        val end = length
        if (tag.start >= end) return
        when (name) {
          "b", "strong" -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), tag.start, end)
          "i", "em" -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), tag.start, end)
          "a" -> tag.href?.let { url ->
            addStyle(linkStyle, tag.start, end)
            addLink(LinkAnnotation.Url(url), tag.start, end)
          }
        }
      } else {
        val href = if (name == "a") hrefPattern.find(body)?.groupValues?.get(1) else null
        state.open.addLast(OpenTag(name = name, start = length, href = href))
      }
    }
    else -> Unit
  }
}

private fun decodeEntity(entity: String): String? {
  namedEntities[entity]?.let { return it }
  if (!entity.startsWith("#")) return null
  val code = if (entity.startsWith("#x", ignoreCase = true)) {
    entity.substring(2).toIntOrNull(16)
  } else {
    entity.substring(1).toIntOrNull()
  }
  return code?.let { runCatching { String(Character.toChars(it)) }.getOrNull() }
}
