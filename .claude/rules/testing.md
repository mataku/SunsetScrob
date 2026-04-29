---
paths:
  - "**/*Spec.kt"
  - "**/*Test.kt"
---

# Testing

## Commands

| Type                 | Command                                                                              |
|----------------------|--------------------------------------------------------------------------------------|
| Unit Test            | `./gradlew testDebugUnitTest -PexcludeScreenshotTest=true`                           |
| Screenshot Test      | `./gradlew verifyRoborazziDebug --no-configuration-cache -PonlyScreenshotTest=true`  |
| Instrumentation Test | `./gradlew :app:connectedDebugAndroidTest` (see [`e2e-testing.md`](e2e-testing.md))  |

One test file per class under test.

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

Register `extension(CoroutinesListener())` when testing suspend code.

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

### Required Annotations (CRITICAL)

Annotate every screenshot test with **all three** of:

- `@RunWith(AndroidJUnit4::class)`
- `@GraphicsMode(GraphicsMode.Mode.NATIVE)`
- `@Category(VRT::class)` (marker from `:test_helper:integration`)

`@Category` is what lets the unit-test bucket
(`testDebugUnitTest -PexcludeScreenshotTest=true`) and the screenshot-test
bucket (`verifyRoborazziDebug -PonlyScreenshotTest=true`) include or exclude
screenshot tests via JUnit Platform tags. Vintage maps `@Category(VRT::class)`
to the tag `com.mataku.scrobscrob.test_helper.integration.VRT`, which
`TestConfiguration.kt` filters on. **Without it the test silently runs in the
wrong bucket** — it compiles fine, but ends up in the wrong CI job.

For new VRTs, prefer the `create-vrt` skill to scaffold a test class with
all three annotations and the `captureScreenshot` wiring already in place.

### File Location

```
feature/{name}/src/test/java/com/mataku/scrobscrob/{name}/ui/screen/
└── {Feature}ScreenTest.kt
```

### Test Class Structure

```kotlin
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Category(VRT::class)
class AlbumScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { AlbumContent(/* ... */) },
      fileName = "album_screen.png"
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { AlbumContent(/* ... */) },
      fileName = "album_screen_light.png"
    )
  }
}
```

### captureScreenshot

Use the helper from `:test_helper:integration`:

```kotlin
composeRule.captureScreenshot(
  appTheme = AppTheme.DARK,           // DARK or LIGHT
  content = { Composable() },          // Test target
  fileName = "screen_name.png",        // Save file name
  device = RobolectricDeviceQualifiers.Pixel7,  // Optional
  actionsBeforeCapturing = {}          // Actions before capture
)
```

### Theme-based Testing

- Dark: `fun layout()` → `{screen_name}.png`
- Light: `fun layout_light()` → `{screen_name}_light.png`

### Device Specification

```kotlin
// Tablet
device = RobolectricDeviceQualifiers.MediumTablet

// Landscape
device = "w411dp-h914dp-land-420dpi"
```

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

Helper for Screenshot Tests. Provides `captureScreenshot` extension function
and the `VRT` category marker.

## File Naming

| Type               | Convention               | Example                  |
|--------------------|--------------------------|--------------------------|
| Unit Test          | `{Class}Spec.kt`         | `LoginViewModelSpec.kt`  |
| Screenshot Test    | `{Screen}ScreenTest.kt`  | `AlbumScreenTest.kt`     |
| Screenshot (Dark)  | `{snake_case}.png`       | `album_screen.png`       |
| Screenshot (Light) | `{snake_case}_light.png` | `album_screen_light.png` |
