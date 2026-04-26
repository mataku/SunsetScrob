package com.mataku.scrobscrob.lint

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin

internal val composableStub: TestFile = kotlin(
  """
    package androidx.compose.runtime

    @Target(
      AnnotationTarget.FUNCTION,
      AnnotationTarget.TYPE,
      AnnotationTarget.TYPE_PARAMETER,
      AnnotationTarget.PROPERTY,
      AnnotationTarget.PROPERTY_GETTER,
    )
    annotation class Composable
  """.trimIndent(),
).indented()

internal val previewStub: TestFile = kotlin(
  """
    package androidx.compose.ui.tooling.preview

    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
    @Retention(AnnotationRetention.BINARY)
    annotation class Preview(
      val name: String = "",
    )
  """.trimIndent(),
).indented()

internal val flowStub: TestFile = kotlin(
  """
    package kotlinx.coroutines.flow

    interface Flow<out T>
  """.trimIndent(),
).indented()

internal val stateFlowStub: TestFile = kotlin(
  """
    package kotlinx.coroutines.flow

    interface StateFlow<out T> : Flow<T> {
      val value: T
    }

    interface MutableStateFlow<T> : StateFlow<T> {
      override var value: T
    }

    fun <T> MutableStateFlow(value: T): MutableStateFlow<T> = error("stub")
  """.trimIndent(),
).indented()

internal val viewModelStub: TestFile = kotlin(
  """
    package androidx.lifecycle

    open class ViewModel
  """.trimIndent(),
).indented()

internal val immutableStub: TestFile = kotlin(
  """
    package androidx.compose.runtime

    @Target(AnnotationTarget.CLASS)
    annotation class Immutable
  """.trimIndent(),
).indented()

internal val material3TextStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Text(text: String) {}
  """.trimIndent(),
).indented()

internal val material3ButtonStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Button(onClick: () -> Unit) {}
  """.trimIndent(),
).indented()

internal val material3TextButtonStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun TextButton(onClick: () -> Unit) {}
  """.trimIndent(),
).indented()

internal val material3IconStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Icon(imageVector: Any, contentDescription: String?) {}
  """.trimIndent(),
).indented()

internal val material3IconButtonStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun IconButton(onClick: () -> Unit, content: () -> Unit) {}
  """.trimIndent(),
).indented()

internal val material3IconToggleButtonStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun IconToggleButton(
      checked: Boolean,
      onCheckedChange: (Boolean) -> Unit,
      content: () -> Unit,
    ) {}
  """.trimIndent(),
).indented()

internal val material3HorizontalDividerStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun HorizontalDivider() {}
  """.trimIndent(),
).indented()

internal val material3CircularProgressIndicatorStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun CircularProgressIndicator() {}
  """.trimIndent(),
).indented()

internal val material3SwitchStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Switch(
      checked: Boolean,
      onCheckedChange: (Boolean) -> Unit,
    ) {}
  """.trimIndent(),
).indented()

internal val material3TabRowStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Tab(
      selected: Boolean,
      onClick: () -> Unit,
    ) {}

    fun TabRow(
      selectedTabIndex: Int,
    ) {}

    fun PrimaryTabRow(
      selectedTabIndex: Int,
    ) {}

    object TabRowDefaults {
      fun tabIndicatorOffset() {}
    }
  """.trimIndent(),
).indented()

internal val material3OutlinedTextFieldStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun OutlinedTextField(
      value: String,
      onValueChange: (String) -> Unit,
    ) {}

    object OutlinedTextFieldDefaults {
      fun colors() {}
    }
  """.trimIndent(),
).indented()

internal val material3AlertDialogStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun AlertDialog(
      onDismissRequest: () -> Unit,
      confirmButton: () -> Unit,
    ) {}
  """.trimIndent(),
).indented()

internal val material3PullToRefreshBoxStub: TestFile = kotlin(
  """
    package androidx.compose.material3.pulltorefresh

    fun PullToRefreshBox(
      isRefreshing: Boolean,
      onRefresh: () -> Unit,
      content: () -> Unit,
    ) {}
  """.trimIndent(),
).indented()

internal val material3SuggestionChipStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun SuggestionChip(
      onClick: () -> Unit,
      label: () -> Unit,
    ) {}

    object SuggestionChipDefaults {
      fun suggestionChipBorder() {}
    }

    class ChipColors
  """.trimIndent(),
).indented()

internal val material3FloatingActionButtonStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun FloatingActionButton(
      onClick: () -> Unit,
      content: () -> Unit,
    ) {}

    object FloatingActionButtonDefaults {
      val shape: Any = Any()
    }
  """.trimIndent(),
).indented()

internal val material3ScaffoldStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Scaffold(
      content: () -> Unit,
    ) {}
  """.trimIndent(),
).indented()

internal val material3TopAppBarStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun TopAppBar(
      title: () -> Unit,
    ) {}

    object TopAppBarDefaults {
      fun centerAlignedTopAppBarColors(): Any = Any()
    }

    interface TopAppBarScrollBehavior
  """.trimIndent(),
).indented()

internal val material3ThemeStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    class ColorScheme(
      val background: Any = Any(),
      val onSurface: Any = Any(),
    )

    object MaterialTheme {
      val colorScheme: ColorScheme = ColorScheme()
    }
  """.trimIndent(),
).indented()

internal val material3BottomSheetScaffoldStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun BottomSheetScaffold(
      sheetContent: () -> Unit,
      content: () -> Unit,
    ) {}

    fun rememberBottomSheetScaffoldState(): Any = Any()

    fun rememberStandardBottomSheetState(): Any = Any()

    enum class SheetValue { Hidden, Expanded, PartiallyExpanded }
  """.trimIndent(),
).indented()

internal val material3ModalBottomSheetStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun ModalBottomSheet(
      onDismissRequest: () -> Unit,
      content: () -> Unit,
    ) {}

    fun rememberModalBottomSheetState(): Any = Any()

    class SheetState
  """.trimIndent(),
).indented()

internal val material3SurfaceStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun Surface(content: () -> Unit) {}
  """.trimIndent(),
).indented()

internal val material3SnackbarHostStub: TestFile = kotlin(
  """
    package androidx.compose.material3

    fun SnackbarHost(
      hostState: SnackbarHostState,
    ) {}

    class SnackbarHostState {
      suspend fun showSnackbar(message: String): Unit = Unit
    }
  """.trimIndent(),
).indented()
