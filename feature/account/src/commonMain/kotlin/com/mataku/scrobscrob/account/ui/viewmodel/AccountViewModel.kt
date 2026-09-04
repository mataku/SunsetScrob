package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.account.update.AppUpdateStatus
import com.mataku.scrobscrob.account.update.InAppUpdateManager
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.core.entity.UserInfo
import com.mataku.scrobscrob.data.repository.FileRepository
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import com.mataku.scrobscrob.data.repository.UserRepository
import com.mataku.scrobscrob.data.repository.UsernameRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class AccountViewModel(
  private val usernameRepository: UsernameRepository,
  private val themeRepository: ThemeRepository,
  private val sessionRepository: SessionRepository,
  private val appInfoProvider: AppInfoProvider,
  private val inAppUpdateManager: InAppUpdateManager,
  private val fileRepository: FileRepository,
  private val userRepository: UserRepository,
) : ViewModel() {

  private val decimalFormat = DecimalFormat("#.##")

  val uiState: StateFlow<AccountUiState>
    field = MutableStateFlow(AccountUiState.initialize())

  init {
    val appVersion = appInfoProvider.appVersion()
    viewModelScope.launch {
      val username = usernameRepository.asyncUsername().first() ?: ""
      launch {
        themeRepository.currentTheme()
          .catch {
            uiState.update {
              it.copy(
                theme = AppTheme.DARK,
                appVersion = appVersion
              )
            }
          }
          .collect {
            uiState.update { state ->
              state.copy(
                theme = it,
                appVersion = appVersion
              )
            }
          }
      }

      if (username.isNotEmpty()) {
        launch {
          userRepository.getInfo(
            userName = username
          )
            .catch {
            }
            .collect { userInfo ->
              uiState.update { state ->
                state.copy(
                  userInfo = userInfo
                )
              }
            }
        }
      }

      launch {
        var initialStatusReceived = false
        inAppUpdateManager.updateStatus()
          .catch { }
          .collect { status ->
            val downloadedNow = initialStatusReceived &&
              status == AppUpdateStatus.DOWNLOADED &&
              uiState.value.appUpdateStatus != AppUpdateStatus.DOWNLOADED
            initialStatusReceived = true
            uiState.update { state ->
              state.copy(
                appUpdateStatus = status,
                events = if (downloadedNow) {
                  (state.events + Event.UpdateDownloaded).toImmutableList()
                } else {
                  state.events
                }
              )
            }
          }
      }

      launch {
        fileRepository.cacheImageDirMBSize()
          .catch { }
          .collect { mb ->
            uiState.update {
              it.copy(imageCacheMB = decimalFormat.format(mb))
            }
          }
      }
    }
  }

  fun completeUpdate() {
    viewModelScope.launch {
      runCatching {
        inAppUpdateManager.completeUpdate()
      }
    }
  }

  fun logout() {
    viewModelScope.launch {
      sessionRepository.logout()
        .catch {

        }
        .onCompletion {
          uiState.update {
            it.copy(events = (it.events + Event.Logout).toImmutableList())
          }
        }
        .collect {

        }
    }
  }

  fun popEvent(event: Event) {
    val newEvents = uiState.value.events.filterNot {
      event == it
    }.toImmutableList()
    uiState.update {
      it.copy(
        events = newEvents
      )
    }
  }

  fun clearCache() {
    viewModelScope.launch {
      fileRepository.deleteCacheImageDir()
        .catch { }
        .collect {
          uiState.update {
            it.copy(imageCacheMB = "0")
          }
        }
    }
  }

  @Immutable
  data class AccountUiState(
    val theme: AppTheme?,
    val events: ImmutableList<Event>,
    val appVersion: String,
    val appUpdateStatus: AppUpdateStatus,
    val imageCacheMB: String?,
    val userInfo: UserInfo?
  ) {
    companion object {
      fun initialize() = AccountUiState(
        theme = null,
        events = persistentListOf(),
        appVersion = "",
        appUpdateStatus = AppUpdateStatus.NONE,
        imageCacheMB = null,
        userInfo = null
      )
    }
  }

  @Immutable
  sealed class Event {
    data object Logout : Event()
    data object UpdateDownloaded : Event()
  }
}
