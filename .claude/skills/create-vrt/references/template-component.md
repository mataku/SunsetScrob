# Template: component (no ViewModel)

Use this when the composable takes only inline values / lambdas and doesn't need a ViewModel. Matches the style of `HomeTabsTest`, `SunsetNavigationBarScreenTest`.

## Template

```kotlin
package {{PACKAGE}}

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class {{CLASS_NAME}} {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        {{COMPOSABLE_CALL}}
      },
      fileName = "{{FILE_NAME_SNAKE}}.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
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

Reference: `feature/home/src/jvmTest/kotlin/com/mataku/scrobscrob/home/ui/molecule/HomeTabsTest.kt`.

```kotlin
package com.mataku.scrobscrob.home.ui.molecule

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class HomeTabsTest {
  @Test
  fun layout() {
    captureScreenshot(
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
    captureScreenshot(
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

Add these test methods only when the layout actually differs for the device variant — don't generate them reflexively. Both need `import com.mataku.scrobscrob.test_helper.integration.ScreenshotDevice`.

```kotlin
@Test
fun layout_landscape() {
  captureScreenshot(
    device = ScreenshotDevice.Pixel7Landscape,
    appTheme = AppTheme.DARK,
    content = { {{COMPOSABLE_CALL}} },
    fileName = "{{FILE_NAME_SNAKE}}_landscape.png"
  )
}

@Test
fun layout_tablet() {
  captureScreenshot(
    device = ScreenshotDevice.PixelTablet,
    appTheme = AppTheme.DARK,
    content = { {{COMPOSABLE_CALL}} },
    fileName = "{{FILE_NAME_SNAKE}}_tablet.png"
  )
}
```

To interact before capturing (open a pane, select a row), pass `actionsBeforeCapturing`; it runs with a `ComposeUiTest` receiver, so node finders and `waitForIdle()` are called directly:

```kotlin
actionsBeforeCapturing = {
  onAllNodesWithText("Item 1").onFirst().performClick()
  waitForIdle()
},
```
