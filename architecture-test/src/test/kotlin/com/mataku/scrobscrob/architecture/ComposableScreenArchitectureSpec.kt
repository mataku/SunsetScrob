package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.DescribeSpec

class ComposableScreenArchitectureSpec : DescribeSpec({

  val scope = Konsist.scopeFromProduction()

  describe("Composable screen conventions (CLAUDE.md Rule 4)") {

    it("`*Screen` @Composable functions in feature modules live in `...ui.screen`") {
      scope.functions()
        .withNameEndingWith("Screen")
        .filter { fn -> fn.annotations.any { it.name == "Composable" } }
        .filter { it.resideInPackage("com.mataku.scrobscrob..") }
        // ui_common hosts reusable templates (atomic design), not feature screens.
        .filterNot { it.resideInPackage("..ui_common..") }
        .assertTrue(
          additionalMessage = "`*Screen` composables in feature modules must live in `...ui.screen`. CLAUDE.md Rule 4.",
        ) { it.resideInPackage("..ui.screen..") }
    }

    // Screen visibility (internal) intentionally not enforced — the hub pattern
    // in :feature:home requires TopAlbumsScreen/TopArtistsScreen/ScrobbleScreen
    // to stay public. Revisit if the hub pattern changes.
  }
})
