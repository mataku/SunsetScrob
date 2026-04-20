# Template: Screen with a ViewModel

Use this when the composable is a top-level Screen that takes a ViewModel. Matches the
style of `AccountScreenTest`, `ArtistScreenTest`, `TopAlbumsScreenTest`.

## Approach

In sunsetscrob the standard pattern is:

1. Mock the ViewModel's repository dependencies with `mockk<FooRepository>()`.
2. Stub every `Flow`-returning method the VM will collect with `coEvery { repo.thing() } returns flowOf(...)`.
3. Instantiate the real ViewModel — don't mock the VM itself. Roborazzi captures real render output, so real state is what you want.
4. Pass empty `mockk()` for navigation callbacks when you don't care what they do.

## Template

```kotlin
package {{PACKAGE}}

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
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
  val composeRule = createComposeRule()

  private val {{REPO_FIELD}} = mockk<{{REPO_TYPE}}>()
  // add more mockk<>() fields for every repo the VM @Inject's

  @Before
  fun setUp() {
    coEvery { {{REPO_FIELD}}.{{REPO_METHOD}}() } returns flowOf({{STUB_VALUE}})
    // add more stubs for each flow the VM collects
  }

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        {{SCREEN_CALL_WITH_VM}}
      },
      fileName = "{{FILE_NAME_SNAKE}}.png"
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = {
        {{SCREEN_CALL_WITH_VM}}
      },
      fileName = "{{FILE_NAME_SNAKE}}_light.png"
    )
  }
}
```

## Substitutions

| Placeholder | Meaning |
|---|---|
| `{{PACKAGE}}` | package of the Screen's source file (e.g. `com.mataku.scrobscrob.artist.ui.screen`) |
| `{{CLASS_NAME}}` | e.g. `ArtistScreenTest` (usually the Screen already ends with `Screen`, so just append `Test`) |
| `{{REPO_FIELD}}` | property name for the mocked repo (e.g. `artistRepository`) |
| `{{REPO_TYPE}}` | repository type (e.g. `ArtistRepository`) |
| `{{REPO_METHOD}}` | method the VM calls on the repo |
| `{{STUB_VALUE}}` | realistic test data (entity literal, `persistentListOf(...)` of domain objects, etc.) |
| `{{SCREEN_CALL_WITH_VM}}` | composable invocation passing a VM constructed with the mocked repos, plus `mockk()` for navigation callbacks |
| `{{FILE_NAME_SNAKE}}` | snake_case of the Screen name (e.g. `artist_screen`, `top_albums_screen`) |

## Example (filled in)

Reference: `feature/artist/src/test/java/com/mataku/scrobscrob/artist/ui/screen/ArtistScreenTest.kt` (abridged).

```kotlin
package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.integration.VRT
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class ArtistScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  private val artistRepository = mockk<ArtistRepository>()
  private val artistInfo = ArtistInfo(/* ... */)

  @Before
  fun setUp() {
    every { artistRepository.fetchArtistInfo(any()) } returns flowOf(artistInfo)
  }

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        ArtistScreen(
          viewModel = ArtistViewModel(
            artistRepository = artistRepository,
            savedStateHandle = SavedStateHandle(mapOf("artistName" to "aespa")),
          ),
          navigateToBack = mockk(),
          sharedTransitionScope = mockk(),
          animatedContentScope = mockk(),
        )
      },
      fileName = "artist_screen.png"
    )
  }

  // layout_light() omitted
}
```

## Tips

- If the Screen sits inside a `SharedTransitionLayout` in production, wrap the `content` lambda with `SharedTransitionLayout { AnimatedVisibility(visible = true) { ... } }` the same way the existing tests do. See `TopArtistsScreenTest` for the current pattern.
- Don't use `mockk(relaxed = true)` just to avoid writing stubs — prefer explicit `coEvery {} returns flowOf(...)` so that if the VM starts collecting a new flow, the test fails loudly instead of silently rendering a blank state.
- `SavedStateHandle` with a `mapOf(...)` of expected keys is the correct way to inject deep-link args; don't mock it.
