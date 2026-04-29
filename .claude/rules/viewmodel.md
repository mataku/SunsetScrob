---
paths:
  - "**/*ViewModel.kt"
  - "**/viewmodel/**/*.kt"
---

# ViewModel Conventions

Reference: `feature/home/.../ui/viewmodel/HomeViewModel.kt`, `feature/scrobble/.../ui/viewmodel/ScrobbleViewModel.kt`.

- Annotate with `@Inject`, `@ViewModelKey`, and `@ContributesIntoMap(AppScope::class)`
  (imports from `dev.zacsweers.metro.*` and `dev.zacsweers.metrox.viewmodel.ViewModelKey`).
  Constructor parameters are resolved by Metro; no `@Inject constructor` needed
  when `@Inject` is applied at the class level.
- ViewModels that extend `AndroidViewModel` must pin the bound type
  explicitly with `@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())`
  (otherwise Metro contributes into a `Map<_, AndroidViewModel>`
  multibinding that `MetroViewModelFactory` never reads, and the VM
  drops out of the graph silently).
- ViewModels that need `SavedStateHandle` (usually to read navigation
  arguments) use Metro's assisted-injection pattern instead:
  ```kotlin
  @AssistedInject
  class FooViewModel(
    private val repo: FooRepository,
    @Assisted savedStateHandle: SavedStateHandle,
  ) : ViewModel() {
    // body

    @AssistedFactory
    @ViewModelAssistedFactoryKey(FooViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
      override fun create(extras: CreationExtras): FooViewModel =
        create(extras.createSavedStateHandle())

      fun create(@Assisted savedStateHandle: SavedStateHandle): FooViewModel
    }
  }
  ```
  The screen still calls `metroViewModel<FooViewModel>()` — Metro's
  factory tries `assistedFactoryProviders` first and falls through to
  the regular `viewModelProviders` map. Navigation arguments land in
  `SavedStateHandle` the same way they did under Hilt. References:
  `feature/scrobble/.../TrackViewModel.kt`, `feature/album/.../AlbumViewModel.kt`.
- Expose state via Kotlin 2.3 explicit backing fields (enabled project-wide
  with `-Xexplicit-backing-fields` in `build-logic`'s `KotlinConfiguration`):
  ```kotlin
  val uiState: StateFlow<FooUiState>
    field = MutableStateFlow(FooUiState.initialize())
  ```
  Public API is `StateFlow<T>` (read-only); inside the same class smart-cast
  resolves `uiState` to the `MutableStateFlow<T>` backing field, so
  `uiState.update { ... }` and `uiState.value = ...` continue to work.
  Do not introduce a separate `_state` property + `.asStateFlow()`, and do
  not use Compose `mutableStateOf` for ViewModel state — keep everything on
  Flow so `collectAsStateWithLifecycle()` is the single consumer pattern in
  Screens. Enforced by the `UiStateMustBeStateFlow` Lint detector in
  `:lint-checks`.
- `FooUiState` is a `data class`, annotated `@Immutable`, with `ImmutableList<T>`
  for list fields (`kotlinx.collections.immutable`). Enforced by the
  `UiStateMustBeImmutable` Lint detector in `:lint-checks` (it flags
  `*UiState` data classes nested in a `*ViewModel` that lack `@Immutable`).
- One-shot events are a `sealed class FooUiEvent` carried **inside the state**
  as `events: List<FooUiEvent>`. The UI pops them via a public function on the
  VM — use `popEvent(event)` (or `popEvent()` when the event type is trivial).
  Some older VMs use `consumeEvent(event)`; prefer `popEvent` for new code.
- Launch coroutines only via `viewModelScope.launch { }` or `.launchIn(viewModelScope)`.
  **Do not** create your own `CoroutineScope`, and do not use `GlobalScope`.
- Class name ends with `ViewModel` and extends `androidx.lifecycle.ViewModel`
  (or `AndroidViewModel` when an `Application` dependency is genuinely needed).
- ViewModel visibility is not enforced. `:app` VMs can stay `internal`
  (same compilation unit as `AppGraph`), but feature-module VMs must be
  public — Metro's contribution aggregation runs when `AppGraph` is
  compiled and cannot see `internal` classes across module boundaries,
  so an `internal` feature VM silently drops out of the
  `viewModelProviders` multibinding and fails at runtime with
  `IllegalArgumentException: Unknown model class`. The Screen still
  wires its VM via `metroViewModel()` inside the same module, and
  navigation crosses modules via public `fooGraph()` extensions, not via
  VM types.
