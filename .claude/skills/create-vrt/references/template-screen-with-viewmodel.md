# Template: Screen with a ViewModel

Use this when the composable is a top-level Screen that takes a ViewModel. Matches the style of `ArtistScreenTest`, `AccountScreenTest`, `TopAlbumsScreenTest`.

## Approach

In sunsetscrob the standard pattern is:

1. Mock the ViewModel's repository dependencies with `mockk<FooRepository>()`.
2. Stub every `Flow`-returning method the VM will collect with `every { repo.thing() } returns flowOf(...)` in a `@BeforeEach`.
3. Instantiate the real ViewModel — don't mock the VM itself. Roborazzi captures real render output, so real state is what you want. Prefer the shared fixtures in `:test_helper:integration` (`sampleArtistInfo`, `sampleAlbumInfo`, ...) over hand-written entities.
4. Pass empty lambdas for navigation callbacks. For `AnimatedContentScope` / `AnimatedVisibilityScope` parameters use `mockk(relaxed = true)` and wrap the content in `SharedTransitionLayout { ... }` the way the existing tests do.

## Template

```kotlin
package {{PACKAGE}}

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class {{CLASS_NAME}} {
  private val {{REPO_FIELD}} = mockk<{{REPO_TYPE}}>()
  // add more mockk<>() fields for every repo the VM takes

  @BeforeEach
  fun setup() {
    every { {{REPO_FIELD}}.{{REPO_METHOD}}() } returns flowOf({{STUB_VALUE}})
    // add more stubs for each flow the VM collects
  }

  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        {{SCREEN_CALL_WITH_VM}}
      },
      fileName = "{{FILE_NAME_SNAKE}}.png"
    )
  }

  @Test
  fun layout_light() {
    captureScreenshot(
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

| Placeholder               | Meaning                                                                                                       |
|---------------------------|---------------------------------------------------------------------------------------------------------------|
| `{{PACKAGE}}`             | package of the Screen's source file (e.g. `com.mataku.scrobscrob.artist.ui.screen`)                           |
| `{{CLASS_NAME}}`          | e.g. `ArtistScreenTest` (usually the Screen already ends with `Screen`, so just append `Test`)                |
| `{{REPO_FIELD}}`          | property name for the mocked repo (e.g. `artistRepository`)                                                   |
| `{{REPO_TYPE}}`           | repository type (e.g. `ArtistRepository`)                                                                     |
| `{{REPO_METHOD}}`         | method the VM calls on the repo                                                                               |
| `{{STUB_VALUE}}`          | realistic test data (a `:test_helper:integration` fixture, `persistentListOf(...)` of domain objects, etc.)   |
| `{{SCREEN_CALL_WITH_VM}}` | composable invocation passing a VM constructed with the mocked repos, plus empty lambdas for navigation       |
| `{{FILE_NAME_SNAKE}}`     | snake_case of the Screen name (e.g. `artist_screen`, `top_albums_screen`)                                     |

## Example (filled in)

Reference: `feature/artist/src/jvmTest/kotlin/com/mataku/scrobscrob/artist/ui/screen/ArtistScreenTest.kt` (abridged).

```kotlin
package com.mataku.scrobscrob.artist.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import com.mataku.scrobscrob.artist.ui.navigation.ArtistKey
import com.mataku.scrobscrob.artist.ui.viewmodel.ArtistViewModel
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.ArtistRepository
import com.mataku.scrobscrob.test_helper.integration.captureScreenshot
import com.mataku.scrobscrob.test_helper.integration.fixture.sampleArtistInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("VRT")
class ArtistScreenTest {
  private val artistName = "aespa"
  private val artistRepository = mockk<ArtistRepository>()
  private val animatedContentScope = mockk<AnimatedContentScope>(relaxed = true)

  @BeforeEach
  fun setup() {
    every { artistRepository.artistInfo(artistName) } returns flowOf(sampleArtistInfo)
  }

  @Test
  fun layout() {
    val key = ArtistKey(artistName = artistName, artworkUrl = "", contentId = "")
    val viewModel = ArtistViewModel(artistRepository = artistRepository, key = key)
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = {
        SharedTransitionLayout {
          ArtistScreen(
            viewModel = viewModel,
            onArtistLoadMoreTap = {},
            onBackPressed = {},
            animatedVisibilityScope = animatedContentScope,
            id = ""
          )
        }
      },
      fileName = "artist_screen.png"
    )
  }

  // layout_light() omitted
}
```

## Tips

- Don't use `mockk(relaxed = true)` for repositories just to avoid writing stubs — prefer explicit `every {} returns flowOf(...)` so that if the VM starts collecting a new flow, the test fails loudly instead of silently rendering a blank state. `relaxed = true` is fine for animation scopes.
- ViewModels that need a NavKey take it as a constructor parameter (`key = ArtistKey(...)`); build the real key, don't mock it.
- A ViewModel that holds a `StateFlow` you want to pin to one state (e.g. a settings screen) can be replaced by `mockk { every { uiState } returns MutableStateFlow(...) }`; see `ThemeSelectorScreenTest`.
