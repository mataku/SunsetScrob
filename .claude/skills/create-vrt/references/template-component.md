# Template: component (no ViewModel)

Use this when the composable takes only inline values / lambdas and doesn't need a
ViewModel. Matches the style of `HomeTabsTest`, `SunsetNavigationBarScreenTest`.

## Template

```kotlin
package {{PACKAGE}}

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class {{CLASS_NAME}} {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun layout() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        {{COMPOSABLE_CALL}}
      },
      fileName = "{{FILE_NAME_SNAKE}}.png"
    )
  }

  @Test
  fun layout_light() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        {{COMPOSABLE_CALL}}
      },
      fileName = "{{FILE_NAME_SNAKE}}_light.png"
    )
  }
}
```

## Substitutions

| Placeholder           | Meaning                                                                                                                                                | Example                                           |
|-----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| `{{PACKAGE}}`         | package of the composable's source file                                                                                                                | `com.mataku.scrobscrob.home.ui.molecule`          |
| `{{CLASS_NAME}}`      | `<ComposableName>Test`                                                                                                                                 | `HomeTabsTest`                                    |
| `{{COMPOSABLE_CALL}}` | composable invocation with sensible test values — empty lambdas for callbacks, small constants for primitives, `persistentListOf(...)` for list params | `HomeTabs(selectedChartIndex = 0, onTabTap = {})` |
| `{{FILE_NAME_SNAKE}}` | snake_case of the composable name                                                                                                                      | `home_tabs`, `sunset_navigation_bar`              |

## Example (filled in)

Reference: `feature/home/src/test/java/com/mataku/scrobscrob/home/ui/molecule/HomeTabsTest.kt`.

```kotlin
package com.mataku.scrobscrob.home.ui.molecule

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class HomeTabsTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun layout() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        HomeTabs(
          selectedChartIndex = 0,
          onTabTap = {}
        )
      },
      fileName = "home_tabs.png"
    )
  }

  @Test
  fun layout_light() {
    composeTestRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        HomeTabs(
          selectedChartIndex = 0,
          onTabTap = {}
        )
      },
      fileName = "home_tabs_light.png"
    )
  }
}
```

## Optional variants

Add these test methods only when the layout actually differs for the device variant — don't generate
them reflexively.

```kotlin
@Test
fun layout_landscape() {
  composeTestRule.captureScreenshot(
    device = "w411dp-h914dp-land-420dpi",
    appTheme = AppTheme.DARK,
    content = { {{COMPOSABLE_CALL}} },
    fileName = "{{FILE_NAME_SNAKE}}_landscape.png"
  )
}

@Test
fun layout_tablet() {
  composeTestRule.captureScreenshot(
    device = RobolectricDeviceQualifiers.MediumTablet,
    appTheme = AppTheme.DARK,
    content = { {{COMPOSABLE_CALL}} },
    fileName = "{{FILE_NAME_SNAKE}}_tablet.png"
  )
}
```

Landscape needs no extra import. Tablet needs:

```kotlin
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
```
