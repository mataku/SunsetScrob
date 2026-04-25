package com.mataku.scrobscrob.account.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.requestCompleteUpdate
import com.mataku.scrobscrob.account.AppInfoProvider
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
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
class AccountViewModel(
  private val usernameRepository: UsernameRepository,
  private val themeRepository: ThemeRepository,
  private val sessionRepository: SessionRepository,
  private val appInfoProvider: AppInfoProvider,
  private val appUpdateManager: AppUpdateManager,
  private val fileRepository: FileRepository,
  private val application: Application,
  private val userRepository: UserRepository,
) : AndroidViewModel(application) {

  private val decimalFormat = DecimalFormat("#.##")

  var uiState: MutableStateFlow<AccountUiState> = MutableStateFlow(AccountUiState.initialize())
    private set

  init {
    val username = usernameRepository.username() ?: ""
    val appVersion = appInfoProvider.appVersion()
    viewModelScope.launch {
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
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
          uiState.update {
            it.copy(appUpdateInfo = appUpdateInfo)
          }
        }

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
        appUpdateManager.requestCompleteUpdate()
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

  fun navigateToUiCatalog() {
    appInfoProvider.navigateToUiCatalogIntent(application)
  }

  @Immutable
  data class AccountUiState(
    val theme: AppTheme?,
    val events: ImmutableList<Event>,
    val appVersion: String,
    val appUpdateInfo: AppUpdateInfo?,
    val imageCacheMB: String?,
    val userInfo: UserInfo?
  ) {
    companion object {
      fun initialize() = AccountUiState(
        theme = null,
        events = persistentListOf(),
        appVersion = "",
        appUpdateInfo = null,
        imageCacheMB = null,
        userInfo = null
      )
    }
  }

  @Immutable
  sealed class Event {
    data object Logout : Event()
  }
}
