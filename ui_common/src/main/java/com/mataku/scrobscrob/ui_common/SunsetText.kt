package com.mataku.scrobscrob.ui_common

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

object SunsetText {
  @SuppressLint("ComposeNamingUppercase")
  @Composable
  operator fun invoke(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = SunsetTextStyle.body,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    Text(
      text = text,
      modifier = modifier,
      color = color,
      fontWeight = fontWeight,
      textAlign = textAlign,
      maxLines = maxLines,
      overflow = overflow,
      style = style,
    )
  }

  @SuppressLint("ComposeNamingUppercase")
  @Composable
  operator fun invoke(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = SunsetTextStyle.body,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    Text(
      text = text,
      modifier = modifier,
      color = color,
      fontWeight = fontWeight,
      textAlign = textAlign,
      maxLines = maxLines,
      overflow = overflow,
      style = style,
    )
  }

  @Composable
  fun Body(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.body, color, fontWeight, textAlign, maxLines, overflow)
  }

  @Composable
  fun Body(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.body, color, fontWeight, textAlign, maxLines, overflow)
  }

  @Composable
  fun Label(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.label, color, fontWeight, textAlign, maxLines, overflow)
  }

  @Composable
  fun Label(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.label, color, fontWeight, textAlign, maxLines, overflow)
  }

  @Composable
  fun Title(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.title, color, fontWeight, textAlign, maxLines, overflow)
  }

  @Composable
  fun Headline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(
      text,
      modifier,
      SunsetTextStyle.headline,
      color,
      fontWeight,
      textAlign,
      maxLines,
      overflow
    )
  }

  @Composable
  fun Subtitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(
      text,
      modifier,
      SunsetTextStyle.subtitle,
      color,
      fontWeight,
      textAlign,
      maxLines,
      overflow
    )
  }

  @Composable
  fun Caption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(
      text,
      modifier,
      SunsetTextStyle.caption,
      color,
      fontWeight,
      textAlign,
      maxLines,
      overflow
    )
  }

  @Composable
  fun ButtonLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
  ) {
    invoke(text, modifier, SunsetTextStyle.button, color, fontWeight, textAlign, maxLines, overflow)
  }
}
