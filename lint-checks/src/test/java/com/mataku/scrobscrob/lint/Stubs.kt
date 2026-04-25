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
