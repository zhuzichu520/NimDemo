package com.chuzi.android.nim.core.callback

import com.chuzi.android.nim.core.error.NimError
import com.chuzi.android.nim.core.error.NimThrowable
import com.chuzi.android.widget.log.lumberjack.L
import com.netease.nimlib.sdk.RequestCallback

/**
 * desc IM请求封装
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimRequestCallback<T>(
    private val onSuccessFunc: (T?) -> Unit,
    private val onErrorFunc: ((NimThrowable) -> Unit)? = null,
    private val onFinishFunc: (() -> Unit)? = null
) : RequestCallback<T> {

    override fun onSuccess(any: T?) {
        onSuccessFunc.invoke(any)
        onFinishFunc?.invoke()
    }

    override fun onFailed(code: Int) {
        onErrorFunc?.invoke(
            NimThrowable(
                code,
                NimError.toMessage(code)
            )
        )
        onFinishFunc?.invoke()
    }

    override fun onException(throwable: Throwable) {
        L.tag("NimRequestCallback").e(throwable) { "IM发生异常" }
        onFinishFunc?.invoke()
    }

}