# Architecture

## Overview

Adopts MVVM + Clean Architecture. No UseCase layer; ViewModel directly uses Repository.

### Technology Stack

| Area | Technology |
|------|------------|
| UI | Jetpack Compose + Material 3 |
| State Management | StateFlow + ImmutableList |
| Network | Ktor Client |
| Local DB | SQLDelight, DataStore |
| DI | Dagger Hilt |
| Testing | Kotest, MockK, Roborazzi |

## Module Structure

```
app/                    - Application entry point, Hilt initialization, Navigation
core/                   - Entity definitions (bottom layer, referenced by all modules)
ui_common/              - Shared Composables, theme, colors
data/
  api/                  - Last.fm API client, Endpoint definitions
  db/                   - DataStore (local persistence)
  repository/           - Repository interface + Impl
feature/
  album/                - Album details
  artist/               - Artist details
  auth/                 - Login authentication
  scrobble/             - Scrobble history
  account/              - Account settings
  discover/             - Charts and discovery
  home/                 - Home tab (integrates other features)
```

**Note**: `feature/home` is an integration layer that depends on other feature modules (scrobble, album, artist).

## Dependency Graph

```
feature/* ──→ data/repository ──→ data/api ──→ core
    │                │
    │                └──→ data/db ──→ core
    │
    └──→ ui_common ──→ core
```

### Rules

- feature depends only on `data/repository` (not directly on `data/api` or `data/db`)
- core is the bottom layer, has no dependencies on other modules
- Only `feature/home` can depend on other features

## Implementation Patterns

### ViewModel Pattern

#### UiState

- `data class` + `@Immutable` annotation
- Use `ImmutableList<T>` for lists
- Define `initialize()` factory method in `companion object`

#### StateFlow

- Encapsulate with `MutableStateFlow` + `private set`
- Update state with `update {}`

#### UiEvent

- Define one-time events with `sealed class`
- Hold as a list inside UiState
- Consume with `popEvent()` / `consumeEvent()` after processing

#### Reference Implementation

[ScrobbleViewModel.kt](../feature/scrobble/src/main/java/com/mataku/scrobscrob/scrobble/ui/viewmodel/ScrobbleViewModel.kt)

```kotlin
@HiltViewModel
class ExampleViewModel @Inject constructor(
  private val repository: ExampleRepository
) : ViewModel() {

  var uiState = MutableStateFlow(ExampleUiState.initialize())
    private set

  fun fetchData() {
    viewModelScope.launch {
      repository.fetchItems()
        .onStart { uiState.update { it.copy(isLoading = true) } }
        .onCompletion { uiState.update { it.copy(isLoading = false) } }
        .catch { e ->
          uiState.update {
            it.copy(uiEvents = (it.uiEvents + UiEvent.Error(e)).toImmutableList())
          }
        }
        .collect { items ->
          uiState.update { it.copy(items = items.toImmutableList()) }
        }
    }
  }

  @Immutable
  data class ExampleUiState(
    val isLoading: Boolean,
    val items: ImmutableList<Item>,
    val uiEvents: ImmutableList<UiEvent>
  ) {
    companion object {
      fun initialize() = ExampleUiState(
        isLoading = false,
        items = persistentListOf(),
        uiEvents = persistentListOf()
      )
    }
  }

  @Immutable
  sealed class UiEvent {
    data class Error(val throwable: Throwable) : UiEvent()
  }
}
```

### Repository Pattern

- Define interface and Impl in the same file
- Return `Flow<T>` (suspend fun + flow builder)
- Specify thread with `flowOn(Dispatchers.IO)`
- `@Singleton` + `@Inject constructor`

#### Reference Implementation

[ScrobbleRepository.kt](../data/repository/src/main/java/com/mataku/scrobscrob/data/repository/ScrobbleRepository.kt)

```kotlin
interface ExampleRepository {
  suspend fun fetchItems(): Flow<List<Item>>
}

@Singleton
class ExampleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService
) : ExampleRepository {
  override suspend fun fetchItems() = flow {
    val endpoint = ExampleEndpoint(params = mapOf())
    val response = lastFmService.request(endpoint)
    emit(response.toItems())
  }.flowOn(Dispatchers.IO)
}
```

### Screen Pattern

- Connect ViewModel with `collectAsStateWithLifecycle()`
- Separate Content composable (for Preview / Screenshot Test)

```kotlin
@Composable
fun ExampleScreen(
  viewModel: ExampleViewModel,
  navigateToDetail: (Item) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ExampleContent(
    items = uiState.items,
    onItemTap = navigateToDetail
  )
}

@Composable
private fun ExampleContent(
  items: ImmutableList<Item>,
  onItemTap: (Item) -> Unit
) {
  // UI implementation
}
```

### DI Configuration

| Target | Configuration |
|--------|---------------|
| Repository | Add `@Binds` to [RepositoryModule.kt](../data/repository/src/main/java/com/mataku/scrobscrob/data/repository/di/RepositoryModule.kt) |
| ViewModel | Only `@HiltViewModel` + `@Inject constructor` (no Module needed) |
| DataStore | Provided by [DatabaseModule.kt](../data/db/src/main/java/com/mataku/scrobscrob/data/db/di/DatabaseModule.kt) |

## Convention Plugins

| Plugin ID | Purpose |
|-----------|---------|
| `sunsetscrob.android.application` | For app module |
| `sunsetscrob.android.feature` | For feature / library modules |
| `sunsetscrob.android.compose` | Compose configuration |
| `sunsetscrob.android.dagger` | Hilt DI |
| `sunsetscrob.android.test.screenshot` | Roborazzi tests |

### New Feature Module Example

Reference: [feature/album/build.gradle.kts](../feature/album/build.gradle.kts)

```kotlin
plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("sunsetscrob.android.test.screenshot")
}

android {
  namespace = "com.mataku.scrobscrob.newfeature"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))

  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.coroutines)
  implementation(libs.kotlinx.collection)
}
```

### Rules

- Use convention plugins for common configuration
- Write only module-specific configuration in build.gradle.kts

## Testing

| Type | Framework | Command |
|------|-----------|---------|
| Unit Test | Kotest + MockK | `fastlane test` |
| Screenshot Test | Roborazzi | `fastlane screenshot_test` |

### Screenshot Test

- Create tests per screen
- Test both Light / Dark themes
- `@RunWith(AndroidJUnit4::class)` + `@GraphicsMode(GraphicsMode.Mode.NATIVE)`

```kotlin
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ExampleScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { ExampleContent(...) },
      fileName = "example_screen.png"
    )
  }
}
```
