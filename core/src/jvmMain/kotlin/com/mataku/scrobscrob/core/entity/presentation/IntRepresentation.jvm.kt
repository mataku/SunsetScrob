package com.mataku.scrobscrob.core.entity.presentation

import java.text.NumberFormat
import java.util.Locale

private fun compactFormat(): NumberFormat =
  NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT)

actual fun String.toReadableIntValue(): String =
  runCatching { compactFormat().format(this.toInt()) }.getOrNull() ?: this

actual fun Int.toReadableIntValue(): String =
  runCatching { compactFormat().format(this) }.getOrNull() ?: this.toString()
