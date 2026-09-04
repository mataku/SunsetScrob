package com.mataku.scrobscrob.scrobble.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mataku.scrobscrob.scrobble.generated.resources.Res

private const val EQUALIZER_ANIMATION_PATH = "files/equalizer.json"

@Composable
actual fun EqualizerAnimation(modifier: Modifier) {
  val json by produceState<String?>(initialValue = null) {
    value = Res.readBytes(EQUALIZER_ANIMATION_PATH).decodeToString()
  }
  val spec = json?.let { LottieCompositionSpec.JsonString(it) } ?: return
  val composition by rememberLottieComposition(spec = spec)
  val animationState by animateLottieCompositionAsState(
    composition = composition,
    iterations = LottieConstants.IterateForever
  )
  LottieAnimation(
    composition = composition,
    progress = { animationState },
    modifier = modifier
  )
}
