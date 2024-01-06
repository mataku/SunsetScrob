package com.mataku.scrobscrob.account.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.data.repository.LicenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LicenseViewModel @Inject constructor(
  private val licenseRepository: LicenseRepository
) : ViewModel() {

  var uiState: MutableStateFlow<LicenseUiState> = MutableStateFlow(LicenseUiState())
    private set

  init {
    fetchLicenseList()
  }

  private fun fetchLicenseList() {
    licenseRepository.licenseList()
      .catch {}
      .onEach {
        uiState.update { state ->
          state.copy(
            licenseList = it.toImmutableList()
          )
        }
      }.launchIn(viewModelScope)
  }

  data class LicenseUiState(
    val licenseList: ImmutableList<LicenseArtifact> = persistentListOf()
  )
}
