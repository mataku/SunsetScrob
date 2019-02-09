package com.mataku.scrobscrob.app.model.entity.presentation

sealed class Result<T> {
    data class Progress<T>(var loading: Boolean) : Result<T>()
    data class Success<T>(var data: T) : Result<T>()
    data class Failure<T>(val errorMessageId: Int) : Result<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Result<T> = Progress(isLoading)
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> failure(errorMessageId: Int): Result<T> = Failure(errorMessageId)
    }
}