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
| Constant | UPPER_SNAKE_CASE | `const val MAX_RESULTS = 50` |

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

Reference: `feature/home/.../HomeNavigation.kt`, `app/.../SunsetMainScreen.kt`,
`ui_common/.../navigation/SunsetNavHost.kt`.

- 各 destination は `data class FooKey(...) : SunsetNavKey` で定義し、
  `:feature:*/.../ui/navigation/FooKey.kt` に置く(共通画面の Key は
  `:ui_common/.../navigation/CommonKeys.kt`)
- `SunsetNavKey` 実装クラスは `@Immutable` + `@Serializable` 必須。Konsist で強制。
- 各 feature は `fun SunsetNavBuilder.fooGraph()` を提供し、`:app/SunsetMainScreen.kt`
  が `SunsetTabHost { fooGraph() }` で合成する
- 遷移は `navigate(FooKey(...))`、戻るは `popBackStack()`(どちらも
  `SunsetDestinationScope` のメンバ、destination ブロック内から呼べる)
- `androidx.navigation3.*` の直接 import は `:ui_common` 内のみ許可。Konsist で強制
- ViewModel に navigation 引数を渡すときは `viewModelFor(key)` を使う(`metroViewModel`
  の直接呼び出しは navigation 文脈では禁止)。詳細は `viewmodel.md`

例(Album destination):

```kotlin
fun SunsetNavBuilder.albumGraph() {
  destination<AlbumKey> { key ->
    AlbumScreen(
      viewModel = viewModelFor(key),
      id = key.contentId,
      animatedContentScope = animatedContentScope,
      onBackPressed = ::popBackStack,
      onAlbumLoadMoreTap = { url -> if (url.isNotEmpty()) navigate(WebViewKey(url)) },
    )
  }
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
