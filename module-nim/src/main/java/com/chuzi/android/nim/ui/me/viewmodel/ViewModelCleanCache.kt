package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.global.CacheGlobal

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
class ViewModelCleanCache : ViewModelBase<ArgDefault>() {

    val cacheSize = MutableLiveData<String>()

    fun updateCacheSize() {
        cacheSize.value = CacheGlobal.getCacheSizeString()
    }

}