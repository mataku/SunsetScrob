package com.mataku.scrobscrob.ui_common.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.ui_common.organism.SunsetNavigationBar
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class SunsetNavigationBarScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @Test
  fun layout() = runTest {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              navController = NavHostController(LocalContext.current),
              navigateToAccount = {},
              navigateToDiscover = {},
              navigateToHome = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            // for checking NavigationBar alpha value
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_dark.png"
    )
  }

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @Test
  fun layout_landscape() = runTest {
    composeRule.captureScreenshot(
      device = "w411dp-h914dp-land-420dpi",
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              navController = NavHostController(LocalContext.current),
              navigateToAccount = {},
              navigateToDiscover = {},
              navigateToHome = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            // for checking NavigationBar alpha value
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_dark_landscape.png"
    )
  }

  @Test
  fun layout_light() = runTest {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              navController = NavHostController(LocalContext.current),
              navigateToAccount = {},
              navigateToDiscover = {},
              navigateToHome = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            // for checking NavigationBar alpha value
            Content(
              modifier = Modifier.padding(it)
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_light.png"
    )
  }

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @Test
  fun layout_tablet() = runTest {
    composeRule.captureScreenshot(
      device = RobolectricDeviceQualifiers.MediumTablet,
      appTheme = AppTheme.DARK,
      content = {
        Scaffold(
          modifier = Modifier.fillMaxSize(),
          topBar = {},
          bottomBar = {
            SunsetNavigationBar(
              navController = NavHostController(LocalContext.current),
              navigateToAccount = {},
              navigateToDiscover = {},
              navigateToHome = {},
              modifier = Modifier
                .navigationBarsPadding()
            )
          },
          content = {
            // for checking NavigationBar alpha value
            Content(
              modifier = Modifier
            )
          }
        )
      },
      fileName = "sunset_navigation_bar_tablet.png"
    )
  }

}

@Composable
private fun Content(modifier: Modifier = Modifier) {
  LazyColumn(
    modifier = modifier.fillMaxSize()
  ) {
    items(20) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "$it Item Item Item Item Item Item Item Item Item Item Item Item Item Item Item Item",
          maxLines = 1
        )
      }
    }
  }
}
