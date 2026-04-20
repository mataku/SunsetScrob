---
name: create-vrt
description: >-
  Generate a Roborazzi visual-regression test (VRT) class for a Jetpack Compose composable in
  the sunsetscrob project, with all three required annotations — @RunWith(AndroidJUnit4::class),
  @GraphicsMode(GraphicsMode.Mode.NATIVE), and @Category(VRT::class) — plus the captureScreenshot
  wiring from :test_helper:integration. Use this skill whenever the user asks to add or create a
  VRT, a Roborazzi test, a screenshot test, a visual regression test, スクリーンショットテスト,
  スクショテスト, or VRT 追加 / VRT 作って — or points at a composable and asks for a visual
  regression test. The skill exists specifically to prevent forgetting @Category(VRT::class),
  which silently drops the test into the wrong CI bucket without failing compilation.
allowed-tools:
  - Read
  - Write
  - Grep
  - Glob
  - Bash(./gradlew:*)
---

# Create VRT (Roborazzi screenshot test)

## Why this skill exists

Every Roborazzi test in sunsetscrob must carry three annotations together:

| Annotation | Purpose | Forgetting it… |
|---|---|---|
| `@RunWith(AndroidJUnit4::class)` | JUnit 4 runner Robolectric needs | Test won't run |
| `@GraphicsMode(GraphicsMode.Mode.NATIVE)` | Native graphics for accurate rendering | Rendering is wrong |
| `@Category(VRT::class)` | Platform-tag marker for bucket filtering | **Silent miscategorization** — test leaks into unit-test bucket and/or is skipped by `fastlane screenshot_test` |

The `@Category(VRT::class)` one is the foot-gun. It compiles fine without it; CI classification just quietly breaks. The `:architecture-spec:test` Konsist rule will fail eventually, but the skill prevents that round-trip. (Convention is documented in the project's `CLAUDE.md` under **Rule 8**.)

## Workflow

### 1. Identify the target composable

From the user's request, determine:

- **composable name** — the `@Composable fun` being tested
- **source file & package** — read the file to confirm signature and imports
- **shape** — does it take a ViewModel, or is it a plain component with inline params?
- **module** — e.g. `:feature:album`, `:ui_common`, `:feature:home`. The test lives at `<module>/src/test/java/<package-as-path>/`.

If the request is loose ("add a VRT for the album screen"), `Grep` for `fun AlbumScreen(` to find the source, then `Read` it to confirm parameters and whether it uses `hiltViewModel()`.

### 2. Pick the template

Two templates live in `references/`. Read the one that fits, then adapt it:

- **`references/template-component.md`** — composable without a ViewModel (toolbars, tabs, rows, small organisms). Matches the style of `HomeTabsTest`, `SunsetNavigationBarScreenTest`.
- **`references/template-screen-with-viewmodel.md`** — Screen that needs a real ViewModel with mocked repositories. Matches `AccountScreenTest`, `ArtistScreenTest`, `TopAlbumsScreenTest`.

If the composable sits somewhere in between (e.g. takes a few callbacks but no VM), start from the component template and add what you need — don't force a VM setup where it isn't needed.

### 3. Place the file correctly

- **Package** matches the source file's package (e.g. `com.mataku.scrobscrob.album.ui.screen`).
- **Path** is `<module>/src/test/java/<package-path>/<ClassName>.kt`.
- **Filename ends in `Test.kt`** (enforced by `TestNamingArchitectureSpec`). Prefer `<ComposableName>Test.kt` — e.g. `AlbumScreenTest.kt`, `HomeTabsTest.kt`. Don't add "Screenshot" or "VRT" to the filename; the `@Category` tag already carries that semantic.
- **One test file per class** under test.

### 4. Default variants

Generate these two tests by default — they cover the dark/light theme split that most bugs show up in:

```kotlin
@Test fun layout()       // AppTheme.DARK,  default Pixel 7
@Test fun layout_light() // AppTheme.LIGHT, default Pixel 7
```

Add more variants **only when they'd genuinely catch a class of bug**:

- `layout_landscape()` — `device = "w411dp-h914dp-land-420dpi"`, when the layout reflows for landscape
- `layout_tablet()` — `device = RobolectricDeviceQualifiers.MediumTablet`, when the layout has a tablet-specific branch

Don't reflexively generate all four variants. Extra screenshots are extra CI time and extra baselines to maintain.

### 5. `fileName` convention

The PNG goes to `<module>/screenshots/<fileName>` relative to the test source set. Use snake_case of the class under test, with suffixes for variants:

| Test method | `fileName` |
|---|---|
| `layout()` | `album_screen.png` |
| `layout_light()` | `album_screen_light.png` |
| `layout_landscape()` | `album_screen_landscape.png` |
| `layout_tablet()` | `album_screen_tablet.png` |

### 6. Verify before reporting done

Run the module's tests with the VRT filter to make sure the new test compiles **and** is picked up by the tag filter (not just that it compiles):

```bash
./gradlew <module>:testDebugUnitTest -PonlyScreenshotTest=true
```

Then confirm the result XML was produced for your new test by using `Glob` on
`<module>/build/test-results/testDebugUnitTest/TEST-*<YourNewClass>*.xml`.

If the XML is missing, `@Category(VRT::class)` is probably missing or wrong — re-check the annotations before anything else.

Baseline PNGs: don't record them from this skill. Recording is a human decision (the user reviews whether the baseline actually looks right). Tell the user to run `fastlane record_screenshot_test` themselves when they're ready.

## Pre-flight checklist

Before handing the test back, verify each of these in the file you wrote:

- [ ] `@RunWith(AndroidJUnit4::class)` present
- [ ] `@GraphicsMode(GraphicsMode.Mode.NATIVE)` present
- [ ] `@Category(VRT::class)` present — this is the one people forget
- [ ] Imports: the three annotations + `com.mataku.scrobscrob.test_helper.integration.VRT` + `com.mataku.scrobscrob.test_helper.integration.captureScreenshot`
- [ ] Filename ends with `Test.kt`
- [ ] Package matches the composable's source package
- [ ] `captureScreenshot(...)` called with `appTheme`, `content`, `fileName`
- [ ] No duplicated `fileName` across tests
- [ ] `./gradlew <module>:testDebugUnitTest -PonlyScreenshotTest=true` runs the new test

If any item in the checklist is ambiguous, read the sibling tests in the same module — the codebase's own existing Roborazzi tests are the canonical style reference.
