package com.mataku.scrobscrob.app.model.entity

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

// singleton instance
object RxEventBus {

    private val stream = PublishSubject.create<Any>()

    fun publish(event: Any) {
        stream.onNext(event)
    }

    // Return "Observable"
    // Using ofType method to filter only events specified
    fun <T> create(event: Class<T>): Observable<T> = stream.ofType(event)
}