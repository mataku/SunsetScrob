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

## Package Structure

Directory structure within feature modules:

```
feature/{name}/
└── src/main/java/com/mataku/scrobscrob/{name}/
    ├── ui/
    │   ├── screen/          # Screen Composable
    │   ├── molecule/        # Reusable medium-sized components
    │   ├── component/       # Feature-specific small components
    │   ├── viewmodel/       # ViewModel
    │   └── navigation/      # Navigation definitions
    └── di/                  # DI Module (if needed)
```

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

Screen receives ViewModel, Content receives pure state.

```kotlin
@Composable
fun AlbumScreen(
  viewModel: AlbumViewModel,
  onBackPressed: () -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AlbumContent(
    albumInfo = uiState.albumInfo,
    onBackPressed = onBackPressed
  )
}

@Composable
private fun AlbumContent(
  albumInfo: AlbumInfo?,
  onBackPressed: () -> Unit
) {
  // UI implementation
}
```

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
- Wrap with `SunsetThemePreview`
- Define layout boundaries with `Surface`

```kotlin
@Composable
@Preview(showBackground = true)
private fun AlbumContentPreview() {
  SunsetThemePreview {
    Surface {
      AlbumContent(
        albumInfo = AlbumInfo(...),
        onBackPressed = {}
      )
    }
  }
}
```

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

### NavGraphBuilder Extension Function

```kotlin
fun NavGraphBuilder.albumGraph(
  navController: NavController,
  sharedTransitionScope: SharedTransitionScope,
) {
  composable(
    "${ALBUM_INFO_DESTINATION}?albumName={albumName}&artistName={artistName}",
    arguments = listOf(
      navArgument("albumName") { type = NavType.StringType },
      navArgument("artistName") { type = NavType.StringType }
    )
  ) { ... }
}
```

### NavController Extension Function

Encode parameters with `Uri.encode()`:

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

### Destination Constants

```kotlin
private const val ALBUM_INFO_DESTINATION = "album_detail"
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
