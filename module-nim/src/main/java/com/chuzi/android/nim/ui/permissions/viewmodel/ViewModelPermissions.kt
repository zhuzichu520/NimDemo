package com.chuzi.android.nim.ui.permissions.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.entity.arg.ArgPermissions

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/7 5:55 PM
 * since: v 1.0.0
 */
class ViewModelPermissions : ViewModelBase<ArgPermissions>() {

    val content = MutableLiveData<String>()

}