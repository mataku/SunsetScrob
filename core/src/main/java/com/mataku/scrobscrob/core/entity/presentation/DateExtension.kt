package com.mataku.scrobscrob.core.entity.presentation

import java.util.Date

fun Date.isDefault(): Boolean {
  return this == Date(0)
}

val defaultDate = Date(0)
