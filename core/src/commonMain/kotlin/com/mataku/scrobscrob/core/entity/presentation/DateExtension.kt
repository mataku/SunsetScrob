package com.mataku.scrobscrob.core.entity.presentation

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.isDefault(): Boolean {
  return this == Date(0)
}

val defaultDate = Date(0)

fun Date.toReadableString(): String? {
  val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
  return runCatching {
    dateFormat.format(this)
  }.getOrNull()
}
