package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mataku.scrobscrob.ui_common.generated.resources.Res
import com.mataku.scrobscrob.ui_common.generated.resources.ic_last_fm_logo
import org.jetbrains.compose.resources.DrawableResource

enum class SunsetTab(
  val iconDrawable: DrawableResource?,
  val title: String,
  val icon: ImageVector?,
) {
  HOME(Res.drawable.ic_last_fm_logo, "Home", null),
  DISCOVER(null, "Discover", Icons.Default.Public),
  ACCOUNT(null, "Account", Icons.Default.Settings);
}
