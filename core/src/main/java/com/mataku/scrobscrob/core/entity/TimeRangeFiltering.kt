package com.mataku.scrobscrob.core.entity

enum class TimeRangeFiltering(
  val rawValue: String,
  val uiLabel: String
) {
  OVERALL("overall", "overall"),
  LAST_7_DAYS("7day", "last 7 days"),
  LAST_30_DAYS("1month", "last 30 days"),
  LAST_365_DAYS("12month", "last 365 days")
}
