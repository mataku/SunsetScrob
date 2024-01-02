package com.mataku.scrobscrob.chart.ui

import androidx.compose.runtime.Immutable

@Immutable
enum class ChartType(val index: Int, val tabName: String) {
  TOP_ARTISTS(0, "Artist"),
  TOP_TRACKS(1, "Track");

  companion object {
    fun findByIndex(index: Int): ChartType {
      return ChartType.entries.find { it.index == index } ?: TOP_ARTISTS
    }
  }
}
