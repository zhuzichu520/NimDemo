package com.chuzi.android.nim.core.callback

import com.chuzi.android.nim.core.error.NimError
import com.chuzi.android.nim.core.error.NimThrowable
import com.google.common.base.Optional
import com.netease.nimlib.sdk.RequestCallback
import io.reactivex.rxjava3.core.FlowableEmitter

/**
 * desc IM请求封装
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimRequestCallback<T>(
    private val emitter: FlowableEmitter<Optional<T>>
) : RequestCallback<T> {

    override fun onSuccess(any: T?) {
        emitter.onNext(Optional.fromNullable(any))
        emitter.onComplete()
    }

    override fun onFailed(code: Int) {
        emitter.onError(
            NimThrowable(
                code,
                NimError.toMessage(code)
            )
        )
        emitter.onComplete()
    }

    override fun onException(throwable: Throwable) {
        emitter.onError(throwable)
        emitter.onComplete()
    }

}