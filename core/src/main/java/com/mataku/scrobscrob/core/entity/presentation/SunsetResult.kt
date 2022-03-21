package com.mataku.scrobscrob.core.entity.presentation

sealed class SunsetResult<T> {
    data class Progress<T>(var loading: Boolean) : SunsetResult<T>()
    data class Success<T>(var data: T) : SunsetResult<T>()
    data class Failure<T>(val throwable: Throwable) : SunsetResult<T>()
    object Initialized : SunsetResult<Nothing>()
    class Initialize<T>() : SunsetResult<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): SunsetResult<T> =
            Progress(isLoading)

        fun <T> success(data: T): SunsetResult<T> =
            Success(data)

        fun <T> failure(throwable: Throwable): SunsetResult<T> =
            Failure(throwable)

        fun <T> initialized(): SunsetResult<T> = Initialize()
    }

    fun getOrNull(): T? {
        return if (this is Success) {
            this.data
        } else {
            null
        }
    }

    fun exceptionOrNull(): Throwable? {
        return if (this is Failure) {
            this.throwable
        } else {
            null
        }
    }
}

inline fun <T> SunsetResult<T>.onSuccess(action: (value: T) -> Unit): SunsetResult<T> {
    if (this is SunsetResult.Success) {
        action(this.data)
    }
    return this
}

inline fun <T> SunsetResult<T>.onFailure(action: (exception: Throwable) -> Unit): SunsetResult<T> {
    exceptionOrNull()?.let {
        action(it)
    }
    return this
}

fun <T> Throwable.toResult(): SunsetResult<T> = SunsetResult.failure<T>(this)