package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import kotlin.reflect.KClass

internal val LocalSunsetSharedTransitionScope =
  compositionLocalOf<SharedTransitionScope?> { null }

internal val LocalSunsetNavigator = compositionLocalOf<SunsetNavigator?> { null }

internal interface SunsetNavigator {
  fun navigate(key: SunsetNavKey)
  fun popBackStack()
}

@Composable
fun SunsetNavHost(
  backStack: SunsetNavBackStack,
  modifier: Modifier = Modifier,
  builder: SunsetNavBuilder.() -> Unit,
) {
  val saveableStateHolder = rememberSaveableStateHolder()

  SharedTransitionLayout(modifier = modifier) {
    val sharedTransitionScope: SharedTransitionScope = this

    val navigator = object : SunsetNavigator {
      override fun navigate(key: SunsetNavKey) {
        backStack.keys.add(key)
      }

      override fun popBackStack() {
        if (backStack.keys.isNotEmpty()) {
          backStack.keys.removeAt(backStack.keys.lastIndex)
        }
      }
    }

    CompositionLocalProvider(
      LocalSunsetSharedTransitionScope provides sharedTransitionScope,
      LocalSunsetNavigator provides navigator,
    ) {
      NavDisplay(
        backStack = backStack.keys,
        modifier = Modifier,
        onBack = {
          if (backStack.keys.isNotEmpty()) {
            backStack.keys.removeAt(backStack.keys.lastIndex)
          }
        },
        entryDecorators = listOf(
          rememberSaveableStateHolderNavEntryDecorator(saveableStateHolder),
        ),
        transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
        popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        entryProvider = entryProvider {
          val sunsetBuilder = SunsetNavBuilder(
            entryProviderScope = this,
            onNavigate = { key -> backStack.keys.add(key) },
            onPopBackStack = {
              if (backStack.keys.isNotEmpty()) {
                backStack.keys.removeAt(backStack.keys.lastIndex)
              }
            },
            onReplaceTop = { key ->
              backStack.keys.clear()
              backStack.keys.add(key)
            },
          )
          sunsetBuilder.builder()
        },
      )
    }
  }
}

internal object SunsetEntryRegistrar {
  fun <K : SunsetNavKey> register(
    scope: EntryProviderScope<SunsetNavKey>,
    type: Class<K>,
    content: @Composable SunsetDestinationScope.(K) -> Unit,
  ) {
    val kClass: KClass<K> = type.kotlin
    scope.addEntryProvider(
      clazz = kClass,
      metadata = { _ -> emptyMap() },
    ) { key ->
      val sharedScope = LocalSunsetSharedTransitionScope.current
        ?: error("SunsetNavHost: SharedTransitionScope not provided")
      val navigator = LocalSunsetNavigator.current
        ?: error("SunsetNavHost: SunsetNavigator not provided")
      val animatedScope: AnimatedContentScope = LocalNavAnimatedContentScope.current

      val destinationScope = SunsetDestinationScopeImpl(
        sharedTransitionScope = sharedScope,
        animatedContentScope = animatedScope,
        navigator = navigator,
      )
      destinationScope.content(key)
    }
  }
}

internal class SunsetDestinationScopeImpl(
  sharedTransitionScope: SharedTransitionScope,
  override val animatedContentScope: AnimatedContentScope,
  private val navigator: SunsetNavigator,
) : SunsetDestinationScope, SharedTransitionScope by sharedTransitionScope {

  override fun navigate(key: SunsetNavKey) = navigator.navigate(key)
  override fun popBackStack() = navigator.popBackStack()

  @Composable
  override fun <VM : ViewModel> viewModelFor(key: SunsetNavKey, type: Class<VM>): VM {
    error("viewModelFor implemented in Task 9")
  }
}
