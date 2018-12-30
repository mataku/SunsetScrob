package com.mataku.scrobscrob.app.model.entity.presentation

sealed class Result<T> {
    data class Progress<T>(var loading: Boolean) : Result<T>()
    data class Success<T>(var data: T) : Result<T>()
    data class Failure<T>(val e: ErrorResponse) : Result<T>()
    data class ErrorResponse(val statusCode: Int, val message: String)

    companion object {
        fun <T> loading(isLoading: Boolean): Result<T> = Progress(isLoading)
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> failure(e: ErrorResponse): Result<T> = Failure(e)
    }
}