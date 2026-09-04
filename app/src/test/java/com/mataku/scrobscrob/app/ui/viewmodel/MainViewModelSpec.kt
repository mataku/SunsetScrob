package com.mataku.scrobscrob.app.ui.viewmodel

import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import com.mataku.scrobscrob.test_helper.unit.CoroutinesListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MainViewModelSpec : DescribeSpec({
  extension(CoroutinesListener())

  describe("init") {
    it("runs keystore recovery and backup restore in order before emitting state") {
      val themeRepository = mockk<ThemeRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      val sessionRepository = mockk<SessionRepository>()
      val callOrder = mutableListOf<String>()
      coEvery { sessionRepository.recoverFromKeystoreLossIfNeeded() } answers {
        callOrder.add("recover")
        flowOf(Unit)
      }
      coEvery { sessionRepository.restoreSessionFromBackupIfNeeded() } answers {
        callOrder.add("restore")
        flowOf(Unit)
      }
      coEvery { sessionRepository.backfillSessionBackup() } answers {
        callOrder.add("backfill")
        flowOf(Unit)
      }
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      every { usernameRepository.usernameFlow() } returns flowOf("matakucom")

      val viewModel = MainViewModel(themeRepository, usernameRepository, sessionRepository)

      viewModel.state.filterNotNull().first().username shouldBe "matakucom"
      callOrder.take(2) shouldBe listOf("recover", "restore")
    }

    it("keeps state null until the backup restore completes") {
      val themeRepository = mockk<ThemeRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      val sessionRepository = mockk<SessionRepository>()
      val gate = CompletableDeferred<Unit>()
      coEvery { sessionRepository.recoverFromKeystoreLossIfNeeded() } returns flowOf(Unit)
      coEvery { sessionRepository.restoreSessionFromBackupIfNeeded() } returns flow {
        gate.await()
        emit(Unit)
      }
      coEvery { sessionRepository.backfillSessionBackup() } returns flowOf(Unit)
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      every { usernameRepository.usernameFlow() } returns flowOf("matakucom")

      val viewModel = MainViewModel(themeRepository, usernameRepository, sessionRepository)

      viewModel.state.value.shouldBeNull()
      gate.complete(Unit)
      viewModel.state.filterNotNull().first().username shouldBe "matakucom"
    }

    it("still emits state when the session calls fail") {
      val themeRepository = mockk<ThemeRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      val sessionRepository = mockk<SessionRepository>()
      coEvery { sessionRepository.recoverFromKeystoreLossIfNeeded() } returns flow {
        throw IllegalStateException("keystore broken")
      }
      coEvery { sessionRepository.restoreSessionFromBackupIfNeeded() } returns flow {
        throw IllegalStateException("gms unavailable")
      }
      coEvery { sessionRepository.backfillSessionBackup() } returns flowOf(Unit)
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      every { usernameRepository.usernameFlow() } returns flowOf(null)

      val viewModel = MainViewModel(themeRepository, usernameRepository, sessionRepository)

      val state = viewModel.state.filterNotNull().first()
      state.theme shouldBe AppTheme.DARK
      state.username.shouldBeNull()
    }

    it("does not block state emission on the backfill") {
      val themeRepository = mockk<ThemeRepository>()
      val usernameRepository = mockk<UsernameRepository>()
      val sessionRepository = mockk<SessionRepository>()
      coEvery { sessionRepository.recoverFromKeystoreLossIfNeeded() } returns flowOf(Unit)
      coEvery { sessionRepository.restoreSessionFromBackupIfNeeded() } returns flowOf(Unit)
      coEvery { sessionRepository.backfillSessionBackup() } returns flow {
        CompletableDeferred<Unit>().await()
      }
      coEvery { themeRepository.currentTheme() } returns flowOf(AppTheme.DARK)
      every { usernameRepository.usernameFlow() } returns flowOf("matakucom")

      val viewModel = MainViewModel(themeRepository, usernameRepository, sessionRepository)

      viewModel.state.filterNotNull().first().username shouldBe "matakucom"
    }
  }
})
