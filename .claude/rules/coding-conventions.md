# Coding Conventions

## Naming

### Class

| Type | Convention | Example |
|------|------------|---------|
| ViewModel | `{Feature}ViewModel` | `LoginViewModel`, `AlbumViewModel` |
| Data class | PascalCase | `RecentTrack`, `ChartArtist` |
| Sealed class | PascalCase | `UiEvent`, `ScrobbleUiEvent` |
| Enum | PascalCase | `AccountMenu`, `AppTheme` |

### Function

| Type | Convention | Example |
|------|------------|---------|
| Composable | PascalCase | `LoginScreen`, `AlbumMetaData` |
| Private Composable | PascalCase + `private` | `private fun LoginContent()` |
| Regular function | camelCase | `fetchRecentTracks()`, `authorize()` |

### Variable

| Type | Convention | Example |
|------|------------|---------|
| Local | camelCase | `uiState`, `isLoading` |
| Private property | camelCase + `private` | `private val page = 1` |
| Constant | UPPER_SNAKE_CASE | `const val ALBUM_INFO_DESTINATION` |

## File Naming

| Type | Convention | Example |
|------|------------|---------|
| Screen | `{Feature}Screen.kt` | `LoginScreen.kt` |
| Molecule | `{Component}.kt` | `AlbumMetaData.kt` |
| ViewModel | `{Feature}ViewModel.kt` | `AlbumViewModel.kt` |
| Navigation | `{Feature}Navigation.kt` | `AlbumNavigation.kt` |
| Test (Unit) | `{Class}Spec.kt` | `LoginViewModelSpec.kt` |
| Test (Screenshot) | `{Screen}ScreenTest.kt` | `AlbumScreenTest.kt` |

## Compose

### Screen and Content Separation

Reference: `feature/scrobble/.../ui/screen/ScrobbleScreen.kt`, `feature/home/.../ui/screen/HomeScreen.kt`.

- Top-level composable is `fun FooScreen(viewModel: FooViewModel = metroViewModel(), navigateToX: (...) -> Unit, modifier: Modifier = Modifier)`.
  It is stateful: collects state with `.collectAsStateWithLifecycle()` and owns
  navigation callbacks.
- A `private fun FooContent(...)` takes **data only** (no ViewModel), is previewable, and contains the Compose layout.
- Prefer declaring the top-level Screen composable as `internal` when
  feasible (Screens aren't consumed across modules beyond the hub pattern).
  Not currently enforced by Konsist — the hub pattern in `:feature:home`
  forces `TopAlbumsScreen`, `TopArtistsScreen`, `ScrobbleScreen` to stay
  public, and shared screens under `:ui_common` (e.g. `WebViewScreen`) are
  deliberately public as they are reused across modules.
- Handle one-shot events in the Screen with
  `LaunchedEffect(uiState.events) { uiState.events.firstOrNull()?.let { event -> ...; viewModel.popEvent(event) } }`.

### Modifier

- Always include `modifier: Modifier = Modifier` as a parameter
- Use `then()` for conditional Modifiers

```kotlin
modifier = Modifier
  .then(
    if (condition) {
      Modifier.clickable { onClick() }
    } else {
      Modifier
    }
  )
  .fillMaxWidth()
```

### Preview

- Define as `private fun`
- Wrap with `SunsetThemePreview` — it provides a `SunsetSurface` background
  internally, so don't add another `Surface { }` wrapper

```kotlin
@Composable
@Preview(showBackground = true)
private fun AlbumContentPreview() {
  SunsetThemePreview {
    AlbumContent(
      albumInfo = AlbumInfo(/* ... */),
      onBackPressed = {}
    )
  }
}
```

`@Preview` composables must be declared `private`. Enforced by the
`PreviewNotPrivate` Lint detector in `:lint-checks`. Suppress with
`@Suppress("PreviewNotPrivate")` only for genuinely shared previews.

### internal fun

Add `internal` to Composables not exposed outside the module:

```kotlin
internal fun ChartCell(
  name: String,
  imageUrl: String?,
  modifier: Modifier = Modifier
)
```

## Data Class

- Add `@Immutable` annotation
- Use `ImmutableList<T>` for lists

```kotlin
@Immutable
data class RecentTrack(
  val artistName: String,
  val images: ImmutableList<Image>,
  val albumName: String,
  val name: String,
  val url: String,
  val date: String? = null
)
```

## Navigation

Reference: `feature/home/.../HomeNavigation.kt`, `app/.../NavigationGraph.kt`.

- Routes are `const val FOO_DESTINATION = "foo"` strings. **Do not** introduce
  type-safe `Nav*` sealed classes unilaterally; follow the existing string
  route pattern.
- Each feature exposes `fun NavGraphBuilder.fooGraph(...)`; `:app` composes them
  in `NavigationGraph.kt`.
- Navigate helpers are extensions on `NavController`: `fun NavController.navigateToFoo(...)`.
- Deep link arguments are read in the VM via
  `savedStateHandle.get<String>("artistName")` etc.
- Encode parameters with `Uri.encode()` when building navigate URLs:

```kotlin
fun NavController.navigateToAlbumInfo(
  albumName: String,
  artistName: String
) {
  val encodedAlbumName = Uri.encode(albumName)
  val encodedArtistName = Uri.encode(artistName)
  navigate("${ALBUM_INFO_DESTINATION}?albumName=${encodedAlbumName}&artistName=${encodedArtistName}")
}
```

## File Order

Order of definitions within a file:

1. Package declaration
2. Import
3. Public Composable / Class
4. Private Content functions
5. Private Helper functions
6. Preview functions (last)

## Security

### Sensitive Information (MUST)

**MUST NOT** hardcode sensitive information directly in source files:

- API keys
- Access tokens
- Secrets
- Passwords
- Private keys

Store sensitive values in `local.properties` and access via `BuildConfig`:

```kotlin
// local.properties
API_KEY=your_api_key
SHARED_SECRET=your_shared_secret

// build.gradle.kts
android {
  defaultConfig {
    buildConfigField("String", "API_KEY", "\"${properties["API_KEY"]}\"")
  }
}

// Usage in code
val apiKey = BuildConfig.API_KEY
```
