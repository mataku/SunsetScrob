package com.mataku.scrobscrob.core.entity.presentation

import android.icu.text.CompactDecimalFormat
import java.util.Locale

private fun compactFormat(): CompactDecimalFormat =
  CompactDecimalFormat.getInstance(Locale.ENGLISH, CompactDecimalFormat.CompactStyle.SHORT)

actual fun String.toReadableIntValue(): String =
  runCatching { compactFormat().format(this.toInt()) }.getOrNull() ?: this

actual fun Int.toReadableIntValue(): String =
  runCatching { compactFormat().format(this) }.getOrNull() ?: this.toString()
