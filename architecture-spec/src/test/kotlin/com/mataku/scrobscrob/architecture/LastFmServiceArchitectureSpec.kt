package com.mataku.scrobscrob.architecture

import com.lemonappdev.konsist.api.Konsist
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty

class LastFmServiceArchitectureSpec : DescribeSpec({

  describe("LastFmService.rawRequest usage") {
    it("only LastFmService.kt may call rawRequest(); production code must use the typed request(endpoint) extension") {
      val violations = Konsist.scopeFromProject().files
        .filter { it.path.contains("/src/main/") }
        .filterNot { it.path.endsWith("/data/api/LastFmService.kt") }
        .filter { it.text.contains("rawRequest(") }

      withClue(
        "Production code must call `LastFmService.request(endpoint)`; `rawRequest` is the " +
          "type-erased hook that only `LastFmService.kt` and MockK-based specs may use. " +
          "Offending files:\n" +
          violations.joinToString("\n") { it.path },
      ) { violations.shouldBeEmpty() }
    }
  }
})
