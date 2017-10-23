package com.mataku.scrobscrob.app.model.entity

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

// singleton instance
object RxEventBus {

    // subscriber and observer
    private val stream = PublishSubject.create<Any>()

    fun post(event: Any) {
        stream.onNext(event)
    }

    // Return Observable
    // Using ofType method to filter only events specified
    fun <T> stream(event: Class<T>): Observable<T> = stream.ofType(event)
}