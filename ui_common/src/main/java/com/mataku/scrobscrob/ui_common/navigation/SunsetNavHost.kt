package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.animation.AnimatedContentScope
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
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
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
        backStack.entries.add(SunsetNavEntry(key))
      }

      override fun popBackStack() {
        if (backStack.entries.isNotEmpty()) {
          backStack.entries.removeAt(backStack.entries.lastIndex)
        }
      }
    }

    CompositionLocalProvider(
      LocalSunsetSharedTransitionScope provides sharedTransitionScope,
      LocalSunsetNavigator provides navigator,
    ) {
      NavDisplay(
        backStack = backStack.entries,
        modifier = Modifier,
        onBack = {
          if (backStack.entries.isNotEmpty()) {
            backStack.entries.removeAt(backStack.entries.lastIndex)
          }
        },
        entryDecorators = listOf(
          rememberSaveableStateHolderNavEntryDecorator(saveableStateHolder),
        ),
        transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
        popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        entryProvider = entryProvider {
          val handlers =
            mutableMapOf<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>()
          val sunsetBuilder = SunsetNavBuilder(
            handlers = handlers,
            onNavigate = navigator::navigate,
            onPopBackStack = navigator::popBackStack,
          )
          sunsetBuilder.builder()

          addEntryProvider(
            clazz = SunsetNavEntry::class,
            metadata = { _ -> emptyMap() },
          ) { entry ->
            val handler = handlers[entry.key::class]
              ?: error("No destination registered for ${entry.key::class}")
            val sharedScope = LocalSunsetSharedTransitionScope.current
              ?: error("SunsetNavHost: SharedTransitionScope not provided")
            val nav = LocalSunsetNavigator.current
              ?: error("SunsetNavHost: SunsetNavigator not provided")
            val animatedScope: AnimatedContentScope = LocalNavAnimatedContentScope.current

            val destinationScope = SunsetDestinationScopeImpl(
              sharedTransitionScope = sharedScope,
              animatedContentScope = animatedScope,
              navigator = nav,
            )
            destinationScope.handler(entry.key)
          }
        },
      )
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

  /**
   * Looks up a Metro-managed ViewModel scoped to this destination's NavKey.
   *
   * Implementation note: Currently uses Option C (Activity-scoped ViewModelStore +
   * `key = navKey.toString()`) because Nav3 1.1.1 does not provide a
   * `rememberViewModelStoreOwnerNavEntryDecorator`. Two consequences:
   *  - VMs are GC'd only when the host Activity is destroyed (no per-entry cleanup on pop).
   *  - Two entries with the same NavKey value share a VM. This app does not push
   *    duplicate detail screens today, but be aware if that changes.
   * Replace this implementation when Nav3 ships per-entry VM scoping.
   */
  @Composable
  override fun <VM : ViewModel> viewModelFor(key: SunsetNavKey, type: Class<VM>): VM {
    val owner = LocalViewModelStoreOwner.current
      ?: error("No ViewModelStoreOwner in composition — is this called inside SunsetNavHost?")
    val baseExtras = if (owner is HasDefaultViewModelProviderFactory) {
      owner.defaultViewModelCreationExtras
    } else {
      androidx.lifecycle.viewmodel.CreationExtras.Empty
    }
    val extras = MutableCreationExtras(baseExtras).apply {
      set(SunsetNavKeyExtra, key)
    }
    val factory = LocalMetroViewModelFactory.current
    return viewModel(
      modelClass = type.kotlin,
      viewModelStoreOwner = owner,
      key = key.toString(),
      factory = factory,
      extras = extras,
    )
  }
}
