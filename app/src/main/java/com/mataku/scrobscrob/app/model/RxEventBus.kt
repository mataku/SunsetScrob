package com.mataku.scrobscrob.app.model

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

// singleton instance
object RxEventBus {

    // subscriber and observer
    private val subject = PublishSubject.create<Any>()

    fun post(event: Any) {
        subject.onNext(event)
    }

    // Return Observable
    // Using ofType method to filter only events specified
    fun <T> stream(event: Class<T>): Observable<T> = subject.hide().ofType(event)
}