package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.libs.tool.byteCountToDisplaySizeTwo
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.domain.UseCaseGetFileSize
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.global.CacheGlobal

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
class ViewModelCleanCache : ViewModelBase<ArgDefault>() {

    val displayChild = MutableLiveData(1)

    private val useCaseGetFileSize by lazy {
        UseCaseGetFileSize()
    }

    val cacheSize = MutableLiveData<String>()

    fun updateCacheSize() {
        useCaseGetFileSize.execute(CacheGlobal.getBaseDiskCacheDir())
            .subscribe {
                cacheSize.value = byteCountToDisplaySizeTwo(it)
                displayChild.value = 0
            }
    }

}