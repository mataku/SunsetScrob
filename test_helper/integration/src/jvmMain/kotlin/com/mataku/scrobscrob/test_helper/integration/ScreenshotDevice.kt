package com.mataku.scrobscrob.test_helper.integration

enum class ScreenshotDevice(
  val widthPx: Float,
  val heightPx: Float,
  val density: Float,
) {
  Pixel7(widthPx = 1080f, heightPx = 2400f, density = 2.625f),
  Pixel7Landscape(widthPx = 2400f, heightPx = 1080f, density = 2.625f),
  PixelTablet(widthPx = 2560f, heightPx = 1600f, density = 2f),
}
