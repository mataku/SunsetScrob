package com.mataku.scrobscrob.ui_common

sealed class SunsetUiState<T> {
  data class Success<T>(var data: T) : SunsetUiState<T>()
  data class Failure<T>(val throwable: Throwable) : SunsetUiState<T>()
  object Loading : SunsetUiState<Nothing>()
  object Initialize : SunsetUiState<Nothing>()

  companion object {
    fun <T> loading(): SunsetUiState<Nothing> = Loading
    fun <T> success(data: T): SunsetUiState<T> = Success(data)
    fun <T> failure(throwable: Throwable): SunsetUiState<T> = Failure(throwable)
    fun <T> initialize(): SunsetUiState<Nothing> = Initialize
  }
}
