package com.mataku.scrobscrob.core.entity.presentation

import android.icu.text.CompactDecimalFormat
import java.util.Locale

fun String.toReadableIntValue(): String {
  return runCatching {
    val compactDecimalFormat = CompactDecimalFormat.getInstance(
      Locale.ENGLISH,
      CompactDecimalFormat.CompactStyle.SHORT
    )
    compactDecimalFormat.format(this.toInt())
  }.getOrNull() ?: this
}

fun Int.toReadableIntValue(): String {
  return runCatching {
    val compactDecimalFormat = CompactDecimalFormat.getInstance(
      Locale.ENGLISH,
      CompactDecimalFormat.CompactStyle.SHORT
    )
    compactDecimalFormat.format(this)
  }.getOrNull() ?: this.toString()
}
