package com.mataku.scrobscrob.ui_common.component.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.android.showkase.annotation.ShowkaseComposable
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@JvmInline
value class SunsetTopAppBarScrollBehavior internal constructor(
  internal val delegate: TopAppBarScrollBehavior,
) {
  val nestedScrollConnection: NestedScrollConnection
    get() = delegate.nestedScrollConnection
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSunsetTopAppBarScrollBehavior(): SunsetTopAppBarScrollBehavior {
  return SunsetTopAppBarScrollBehavior(
    delegate = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunsetTopAppBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  scrollBehavior: SunsetTopAppBarScrollBehavior? = null,
) {
  val background = LocalAppTheme.current.backgroundColor()
  TopAppBar(
    title = title,
    modifier = modifier,
    navigationIcon = navigationIcon,
    scrollBehavior = scrollBehavior?.delegate,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = background,
      scrolledContainerColor = background,
    ),
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@ShowkaseComposable(name = "SunsetTopAppBar", group = "Design system")
@Composable
internal fun SunsetTopAppBarPreview() {
  SunsetThemePreview {
    SunsetTopAppBar(
      title = { SunsetText.Title(text = "Top App Bar") },
      navigationIcon = {
        SunsetIconButton(onClick = {}) {
          SunsetIcon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "back",
          )
        }
      },
    )
  }
}
