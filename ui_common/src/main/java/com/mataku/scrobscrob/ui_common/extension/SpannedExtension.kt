package com.mataku.scrobscrob.ui_common.extension

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.accentColor

private const val URL_TAG = "tag"

@Composable
fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
  val spanned = this@toAnnotatedString
  append(spanned.toString())
  getSpans(0, spanned.length, Any::class.java).forEach { span ->
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    when (span) {
      is URLSpan -> {
        addStyle(SpanStyle(color = LocalAppTheme.current.accentColor()), start, end)
        addStringAnnotation(
          tag = URL_TAG,
          annotation = span.url,
          start,
          end
        )
      }

      is StyleSpan -> when (span.style) {
        Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
        Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
        Typeface.BOLD_ITALIC -> addStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
          ),
          start,
          end
        )
      }

      is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
    }
  }
}

fun AnnotatedString.url(position: Int, onInvoke: (String) -> Unit) =
  getStringAnnotations(
    tag = URL_TAG,
    start = position,
    end = position
  ).firstOrNull()?.item?.let { item ->
    onInvoke.invoke(item)
  }
