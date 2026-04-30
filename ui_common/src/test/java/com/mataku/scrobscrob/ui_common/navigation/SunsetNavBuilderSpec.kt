package com.mataku.scrobscrob.ui_common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Immutable
@Serializable
private data object FooKey : SunsetNavKey

@Immutable
@Serializable
private data class BarKey(val id: String) : SunsetNavKey

class SunsetNavBuilderSpec : DescribeSpec({
  describe("SunsetNavBuilder.destination") {
    it("registers a handler keyed by the reified K type") {
      val handlers =
        mutableMapOf<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>()
      val builder = SunsetNavBuilder(
        handlers = handlers,
        onNavigate = {},
        onPopBackStack = {},
      )

      builder.destination<FooKey> { /* no-op */ }
      builder.destination<BarKey> { /* no-op */ }

      handlers.shouldContainKey(FooKey::class)
      handlers.shouldContainKey(BarKey::class)
      handlers.size shouldBe 2
    }

    it("keeps a single entry when destination<K> is registered twice for the same K") {
      val handlers =
        mutableMapOf<KClass<out SunsetNavKey>, @Composable SunsetDestinationScope.(SunsetNavKey) -> Unit>()
      val builder = SunsetNavBuilder(
        handlers = handlers,
        onNavigate = {},
        onPopBackStack = {},
      )

      builder.destination<FooKey> { /* first */ }
      builder.destination<FooKey> { /* second */ }

      handlers.size shouldBe 1
      handlers.shouldContainKey(FooKey::class)
    }
  }
})
