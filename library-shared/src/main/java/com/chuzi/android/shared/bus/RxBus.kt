package com.chuzi.android.shared.bus

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2020-02-17
 * Time: 16:39
 */
object RxBus {

    private val mBus = PublishSubject.create<Any>().toSerialized()

    fun post(event: Any) {
        mBus.onNext(event)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }
}