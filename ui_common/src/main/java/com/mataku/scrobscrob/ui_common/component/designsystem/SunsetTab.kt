package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mataku.scrobscrob.ui_common.R

enum class SunsetTab(
  @param:DrawableRes val iconDrawable: Int?,
  val title: String,
  val icon: ImageVector?,
) {
  HOME(R.drawable.ic_last_fm_logo, "Home", null),
  DISCOVER(null, "Discover", Icons.Default.Public),
  ACCOUNT(null, "Account", Icons.Default.Settings);
}
