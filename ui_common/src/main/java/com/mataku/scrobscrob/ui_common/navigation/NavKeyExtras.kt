package com.mataku.scrobscrob.ui_common.navigation

import androidx.lifecycle.viewmodel.CreationExtras

internal val SunsetNavKeyExtra = object : CreationExtras.Key<SunsetNavKey> {}

fun CreationExtras.requireSunsetNavKey(): SunsetNavKey =
  this[SunsetNavKeyExtra] ?: error("SunsetNavKey extra not found")

inline fun <reified K : SunsetNavKey> CreationExtras.requireKey(): K =
  requireSunsetNavKey() as K
