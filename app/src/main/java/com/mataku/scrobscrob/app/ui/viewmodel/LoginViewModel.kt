package com.mataku.scrobscrob.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mataku.scrobscrob.core.entity.presentation.SunsetResult
import com.mataku.scrobscrob.data.repository.MobileSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: MobileSessionRepository) : ViewModel() {

    private val _authorizeResult = MutableStateFlow<SunsetResult<Unit>>(SunsetResult.initialized())
    val authorizeResult: StateFlow<SunsetResult<Unit>> = _authorizeResult

    fun authorize(username: String, password: String) {
//        viewModelScope.launch {
//            mobileSessionRepository.authorize(userName = username, password = password)
//                .catch {
//                    _authorizeResult.emit(
//                        it.toResult()
//                    )
//                }
//                .collect {
//                    _authorizeResult.emit(SunsetResult.success(Unit))
//                }
//        }
    }
}