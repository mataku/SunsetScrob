---
name: create-vrt
description: >-
  Generate a Roborazzi visual-regression test (VRT) class for a Compose composable in the
  sunsetscrob project: a JUnit 5 class under src/jvmTest tagged @Tag("VRT") that calls the
  captureScreenshot function from :test_helper:integration and renders on the JVM through Compose
  Desktop. Use this skill whenever the user asks to add or create a VRT, a Roborazzi test, a
  screenshot test, a visual regression test, スクリーンショットテスト, スクショテスト, or VRT 追加 /
  VRT 作って — or points at a composable and asks for a visual regression test. The skill exists
  specifically to prevent forgetting @Tag("VRT"), which silently drops the test into the unit-test
  bucket without failing compilation.
allowed-tools:
  - Read
  - Write
  - Grep
  - Glob
  - Bash(./gradlew:*)
---

# Create VRT (Roborazzi screenshot test)

## Why this skill exists

Every Roborazzi test in sunsetscrob is a plain JUnit 5 class that renders on the JVM (Compose Desktop, Skia). Two things are required:

| Requirement | Purpose | Forgetting it… |
|---|---|---|
| `@Tag("VRT")` on the class (`org.junit.jupiter.api.Tag`) | Tag the `-PonlyScreenshotTest=true` / `-PexcludeScreenshotTest=true` filters select on | **Silent miscategorization** — the test runs in the unit-test bucket and never in `verifyRoborazziJvm` |
| `captureScreenshot(...)` from `:test_helper:integration` (`jvmMain`) | Sets up the theme, disables ripples, picks the device size and writes the PNG | Rendering is wrong or the golden lands in the wrong place |

The `@Tag("VRT")` one is the foot-gun. It compiles fine without it; CI classification just quietly breaks. `ScreenshotTestArchitectureSpec` in `:architecture-spec` fails eventually, but the skill prevents that round-trip. The conventions are documented in `.claude/rules/testing.md` under "Screenshot Test (VRT)".

## Workflow

### 1. Identify the target composable

From the user's request, determine:

- **composable name** — the `@Composable fun` being tested
- **source file & package** — read the file to confirm signature and imports
- **shape** — does it take a ViewModel, or is it a plain component with inline params?
- **module** — e.g. `:feature:album`, `:ui_common`, `:feature:home`. The test lives at `<module>/src/jvmTest/kotlin/<package-as-path>/`.

If the request is loose ("add a VRT for the album screen"), `Grep` for `fun AlbumScreen(` to find the source, then `Read` it to confirm parameters and whether it takes a ViewModel.

### 2. Pick the template

Two templates live in `references/`. Read the one that fits, then adapt it:

- **`references/template-component.md`** — composable without a ViewModel (toolbars, tabs, rows, small organisms). Matches the style of `HomeTabsTest`, `SunsetNavigationBarScreenTest`.
- **`references/template-screen-with-viewmodel.md`** — Screen that needs a real ViewModel with mocked repositories. Matches `ArtistScreenTest`, `AccountScreenTest`, `TopAlbumsScreenTest`.

If the composable sits somewhere in between (e.g. takes a few callbacks but no VM), start from the component template and add what you need — don't force a VM setup where it isn't needed.

### 3. Place the file correctly

- **Package** matches the source file's package (e.g. `com.mataku.scrobscrob.album.ui.screen`).
- **Path** is `<module>/src/jvmTest/kotlin/<package-path>/<ClassName>.kt`.
- **Filename ends in `Test.kt`** (enforced by `TestNamingArchitectureSpec`). Prefer `<ComposableName>Test.kt` — e.g. `AlbumScreenTest.kt`, `HomeTabsTest.kt`. Don't add "Screenshot" or "VRT" to the filename; the `@Tag` already carries that semantic.
- **One test file per class** under test.

### 4. Default variants

Generate these two tests by default — they cover the dark/light theme split that most bugs show up in:

```kotlin
@Test fun layout()       // AppTheme.DARK,  ScreenshotDevice.Pixel7 (default)
@Test fun layout_light() // AppTheme.LIGHT, ScreenshotDevice.Pixel7 (default)
```

Add more variants **only when they'd genuinely catch a class of bug**:

- `layout_landscape()` — `device = ScreenshotDevice.Pixel7Landscape`, when the layout reflows for landscape
- `layout_tablet()` — `device = ScreenshotDevice.PixelTablet`, when the layout has a tablet-specific branch (list-detail panes, wider grids)

Don't reflexively generate all four variants. Extra screenshots are extra CI time and extra goldens to maintain.

### 5. `fileName` convention

The PNG goes to `<module>/screenshot/<fileName>`. Use snake_case of the class under test, with suffixes for variants:

| Test method | `fileName` |
|---|---|
| `layout()` | `album_screen.png` |
| `layout_light()` | `album_screen_light.png` |
| `layout_landscape()` | `album_screen_landscape.png` |
| `layout_tablet()` | `album_screen_tablet.png` |

### 6. Verify before reporting done

Run the module's tests with the VRT filter to make sure the new test compiles **and** is picked up by the tag filter (not just that it compiles):

```bash
./gradlew <module>:jvmTest -PonlyScreenshotTest=true
```

Then confirm the result XML was produced for your new test by using `Glob` on `<module>/build/test-results/jvmTest/TEST-*<YourNewClass>*.xml`.

If the XML is missing, `@Tag("VRT")` is probably missing or wrong — re-check the annotation before anything else. (`sunsetscrob.test.screenshot` sets `failOnNoDiscoveredTests` under `-PonlyScreenshotTest=true`, so a module whose only VRT lost its tag fails the build outright.)

Goldens: don't record them from this skill. Recording is a human decision (the user reviews whether the golden actually looks right). Tell the user to run `./gradlew <module>:recordRoborazziJvm --no-configuration-cache -PonlyScreenshotTest=true` themselves when they're ready, then `verifyRoborazziJvm` with the same flags to confirm.

## Pre-flight checklist

Before handing the test back, verify each of these in the file you wrote:

- [ ] `@Tag("VRT")` present on the class — this is the one people forget
- [ ] `@Test` is `org.junit.jupiter.api.Test` (not `org.junit.Test`); setup uses `@BeforeEach`, not `@Before`
- [ ] Imports: `com.mataku.scrobscrob.test_helper.integration.captureScreenshot` (plus `ScreenshotDevice` when a non-default device is used)
- [ ] No `createComposeRule()`, `@RunWith`, `@GraphicsMode` or `@Category` — those were the retired Robolectric form
- [ ] Filename ends with `Test.kt` and lives under `src/jvmTest/kotlin`
- [ ] Package matches the composable's source package
- [ ] `captureScreenshot(...)` called with `appTheme`, `content`, `fileName`
- [ ] No duplicated `fileName` across tests
- [ ] `./gradlew <module>:jvmTest -PonlyScreenshotTest=true` runs the new test

If any item in the checklist is ambiguous, read the sibling tests in the same module — the codebase's own existing Roborazzi tests are the canonical style reference.
