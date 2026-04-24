# Testing

## Commands

| Type | Command |
|------|---------|
| Unit Test | `fastlane test` |
| Screenshot Test | `fastlane screenshot_test` |
| Instrumentation Test | `fastlane android_test` |

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

### Assertions

```kotlin
value shouldBe expected
list.shouldBeEmpty()
boolean.shouldBeFalse()
```

### MockK

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

## Screenshot Test

Uses Roborazzi. Create tests per screen.

### File Location

```
feature/{name}/src/test/java/com/mataku/scrobscrob/{name}/ui/screen/
└── {Feature}ScreenTest.kt
```

### Test Class Structure

```kotlin
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class AlbumScreenTest {
  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun layout() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.DARK,
      content = { AlbumContent(...) },
      fileName = "album_screen.png"
    )
  }

  @Test
  fun layout_light() {
    composeRule.captureScreenshot(
      appTheme = AppTheme.LIGHT,
      content = { AlbumContent(...) },
      fileName = "album_screen_light.png"
    )
  }
}
```

### captureScreenshot

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
    albumInfo = AlbumInfo(...),
    onBackPressed = {}
  )
}
```

## Test Helpers

### test_helper:unit

Helper for Unit Tests. Provides `CoroutinesListener`.

### test_helper:integration

Helper for Screenshot Tests. Provides `captureScreenshot` extension function.

## File Naming

| Type | Convention | Example |
|------|------------|---------|
| Unit Test | `{Class}Spec.kt` | `LoginViewModelSpec.kt` |
| Screenshot Test | `{Screen}ScreenTest.kt` | `AlbumScreenTest.kt` |
| Screenshot (Dark) | `{snake_case}.png` | `album_screen.png` |
| Screenshot (Light) | `{snake_case}_light.png` | `album_screen_light.png` |
