package com.chuzi.android.nim.domain

import com.chuzi.android.libs.tool.sizeOf
import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.chuzi.android.shared.ext.onNextComplete
import io.reactivex.rxjava3.core.Flowable
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:12 PM
 * since: v 1.0.0
 */
class UseCaseGetFileSize :
    UseCase<File, Flowable<Long>>() {

    override fun execute(parameters: File): Flowable<Long> {
        return createFlowable<Long> {
            onNextComplete(sizeOf(parameters))
        }.bindToSchedulers().bindToException()
    }

}