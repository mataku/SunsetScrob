package com.mataku.scrobscrob.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable

/*
  Showkase requires composable previews in the module where ShowkaseRootModule is placed
  TODO: remove
 */
@Preview
@ShowkaseComposable(
  name = "Sample",
  group = "Common",
)
@Composable
fun MainScreenPreview() {
  Text(text = "Hello!")
}
