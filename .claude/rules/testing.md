---
paths:
  - "**/*Spec.kt"
  - "**/*Test.kt"
---

# Testing

## Commands

| Type                 | Command                                                                              |
|----------------------|--------------------------------------------------------------------------------------|
| Unit Test            | `./gradlew jvmTest -PexcludeScreenshotTest=true`                                     |
| Screenshot Test      | `./gradlew verifyRoborazziJvm --no-configuration-cache -PonlyScreenshotTest=true`    |
| Instrumentation Test | `./gradlew :app:connectedDebugAndroidTest` (see [`e2e-testing.md`](e2e-testing.md))  |

One test file per class under test.

## Run new or modified tests before committing

When you add or modify a test, run it locally before committing — for
**every** test type, including slow Gradle-Managed-Device instrumentation
tests. Compile-only verification (`assembleDebugAndroidTest`) is not a
substitute: it only proves the test class builds, not that it passes on a
real device.

A single GMD run takes ~5–15 minutes once the system image is cached;
debugging a broken commit after the fact (failure surfaces in CI or via
the user, root-cause hunt, follow-up commit) costs more than that. Skip
the local run only with explicit user permission and only when the test
itself is not the change under verification.

Pair this with the verification commands in the path-scoped guides:
- VRT: `./gradlew verifyRoborazziJvm --no-configuration-cache -PonlyScreenshotTest=true`
- E2E (phone): `./gradlew :app:pixel6Api35DebugAndroidTest`
- E2E (tablet, `@LargeScreenE2E`): `./gradlew :app:pixelTabletApi35DebugAndroidTest -PincludeLargeScreenE2E=true`

Running `jvmTest` alone (e.g. `-PexcludeScreenshotTest=true`) does not compare goldens — only the `verifyRoborazzi*` tasks do — so a change that can affect a VRT must be checked with the verify task, not just the plain unit-test command.

## Unit Test

Uses Kotest + MockK.

### File Location

```
feature/{name}/src/test/java/com/mataku/scrobscrob/{name}/
├── ui/viewmodel/
│   └── {Feature}ViewModelSpec.kt
└── ...
```

### Test Class Structure

Inherit from `DescribeSpec` and write in BDD style:

```kotlin
class LoginViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("#authorize") {
    context("username is blank") {
      it("should return EmptyUsernameError") {
        val viewModel = LoginViewModel(sessionRepository)
        viewModel.authorize()
        viewModel.uiState.value.events.first() shouldBe UiEvent.EmptyUsernameError
      }
    }
  }
})
```

Register `extension(CoroutinesListener())` when testing ViewModels (where
`viewModelScope` needs a deterministic dispatcher). Repository specs that
collect a `Flow` via Turbine's `.test { ... }` do **not** need it, because
Turbine pumps the flow on its own dispatcher.

### Repository spec

Repositories mock `LastFmService` and capture the `Endpoint` argument:

```kotlin
class AlbumRepositorySpec : DescribeSpec({
  describe("albumInfo") {
    it("builds AlbumInfoEndpoint and maps to AlbumInfo") {
      val service = mockk<LastFmService>()
      val slot = slot<Endpoint<*>>()
      coEvery { service.rawRequest(capture(slot), any()) } returns fakeAlbumInfoResponse

      val repo = AlbumRepositoryImpl(service)
      repo.albumInfo("Drama", "aespa").test {
        awaitItem().albumName shouldBe "Drama"
        awaitComplete()
      }

      slot.captured.shouldBeInstanceOf<AlbumInfoEndpoint>()
      slot.captured.params shouldBe mapOf("album" to "Drama", "artist" to "aespa")
    }
  }
})
```

URL/method/JSON deserialization is verified separately in
`:data:api/src/test/.../endpoint/{Endpoint}Spec.kt` — that is where
`MockEngine` wiring belongs.

### Assertions

```kotlin
value shouldBe expected
list.shouldBeEmpty()
boolean.shouldBeFalse()
```

### MockK

Prefer explicit `mockk<T>()` + `coEvery { } returns ...`. Avoid
`mockk(relaxed = true)` unless the intent is genuinely "ignore all
unused members".

#### Creating Mocks

```kotlin
val repository = mockk<ExampleRepository>()
val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
```

`relaxed = true`: All methods return default values.

#### Stubbing

```kotlin
// suspend function
coEvery {
  repository.fetchItems()
}.returns(flowOf(items))

// regular function
every {
  savedStateHandle.get<String>("key")
}.returns("value")
```

#### Verification

```kotlin
coVerify(exactly = 1) {
  repository.fetchItems()
}
```

### Turbine (Flow Testing)

```kotlin
repository.fetchItems()
  .test {
    awaitItem().let { items ->
      items.size shouldBe 1
      items[0].name shouldBe "expected"
    }
    awaitComplete()
  }
```

### CoroutinesListener

Add `CoroutinesListener` as an extension for ViewModel tests:

```kotlin
class ExampleViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())
  // ...
})
```

## Screenshot Test (VRT)

Uses Roborazzi. Create tests per screen.

### JVM rendering

Screenshot tests live under `src/jvmTest/kotlin`, are JUnit 5 classes and render through Compose Desktop (JVM Skia, not Robolectric). Two things are required:

- `@Tag("VRT")` on the class (`org.junit.jupiter.api.Tag`). This is what `-PonlyScreenshotTest=true` / `-PexcludeScreenshotTest=true` filter on; without it the class runs in the unit-test bucket. Enforced by `ScreenshotTestArchitectureSpec`.
- `captureScreenshot` from `:test_helper:integration` (`jvmMain`), a top-level function, no rule needed.

```kotlin
@Tag("VRT")
class AlbumScreenTest {
  @Test
  fun layout() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { AlbumContent(/* ... */) },
      fileName = "album_screen.png",
    )
  }

  @Test
  fun layout_tablet_two_pane() {
    captureScreenshot(
      appTheme = AppTheme.DARK,
      device = ScreenshotDevice.PixelTablet,
      content = { /* ... */ },
      actionsBeforeCapturing = {
        onAllNodesWithText("Item 1").onFirst().performClick()
        waitForIdle()
      },
      fileName = "album_screen_tablet_two_pane.png",
    )
  }
}
```

`device` is a `ScreenshotDevice` (`Pixel7`, `Pixel7Landscape`, `PixelTablet`). `actionsBeforeCapturing` runs with a `ComposeUiTest` receiver, so node finders and `waitForIdle()` are called directly. Goldens are written to `<module>/screenshot/`.

### Test Target

Test Content instead of Screen (no need to mock ViewModel):

```kotlin
content = {
  AlbumContent(
    albumInfo = AlbumInfo(/* ... */),
    onBackPressed = {},
  )
}
```

## Test Helpers

### test_helper:unit

Helper for Unit Tests. Provides `CoroutinesListener`.

### test_helper:integration

Provides the top-level `captureScreenshot` function and `ScreenshotDevice` (`jvmMain`) and shared fixtures (`commonMain`).

## File Naming

| Type               | Convention               | Example                  |
|--------------------|--------------------------|--------------------------|
| Unit Test          | `{Class}Spec.kt`         | `LoginViewModelSpec.kt`  |
| Screenshot Test    | `{Screen}ScreenTest.kt`  | `AlbumScreenTest.kt`     |
| Screenshot (Dark)  | `{snake_case}.png`       | `album_screen.png`       |
| Screenshot (Light) | `{snake_case}_light.png` | `album_screen_light.png` |

### Determinism guards

- `captureScreenshot` disables Material ripples (`LocalRippleConfiguration provides null`) so a test that clicks in `actionsBeforeCapturing` never captures a half-faded press highlight. Do not re-enable ripples inside a VRT.
- Modules applying `sunsetscrob.test.screenshot` fail when `-PonlyScreenshotTest=true` discovers zero tests (`failOnNoDiscoveredTests`), so a VRT class that lost its `@Tag("VRT")` surfaces as a failed build rather than a silently skipped test.
