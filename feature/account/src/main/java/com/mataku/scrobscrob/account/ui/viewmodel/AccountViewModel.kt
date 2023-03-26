package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.ktx.requestCompleteUpdate
import com.mataku.scrobscrob.account.AppInfoProvider
import com.mataku.scrobscrob.core.entity.AppTheme
import com.mataku.scrobscrob.data.repository.SessionRepository
import com.mataku.scrobscrob.data.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
  private val themeRepository: ThemeRepository,
  private val sessionRepository: SessionRepository,
  private val appInfoProvider: AppInfoProvider,
  private val appUpdateManager: AppUpdateManager
) : ViewModel() {

  var uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.initialize())
    private set

  init {
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

      launch {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
          uiState.update {
            it.copy(appUpdateInfo = appUpdateInfo)
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
            it.copy(event = Event.Logout)
          }
        }
        .collect { }
    }
  }

  fun popEvent() {
    uiState.update {
      it.copy(
        event = null
      )
    }
  }

  data class UiState(
    val theme: AppTheme?,
    val event: Event?,
    val appVersion: String,
    val appUpdateInfo: AppUpdateInfo?
  ) {
    companion object {
      fun initialize() = UiState(
        theme = null,
        event = null,
        appVersion = "",
        appUpdateInfo = null
      )
    }
  }

  sealed class Event {
    object Logout : Event()
  }
}
