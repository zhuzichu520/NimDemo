package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.domain.UseCaseGetUserInfo
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.autoLoading
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.rxjava.rxlife.life
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
class ViewModelMe : ViewModelBase<ArgDefault>() {

    private val userInfo = MutableLiveData<NimUserInfo>()

    private val useCaseGetUserInfo by lazy {
        UseCaseGetUserInfo()
    }

    /**
     * 所有数据
     */
    val items = MutableLiveData<List<Any>>().also {
        val data = ArrayList<Any>()
        data.add(
            ItemViewModelMeUser(this, userInfo)
        )
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_collection,
                R.mipmap.nim_ic_me_collection
            )
        )
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_file,
                R.mipmap.nim_ic_me_file
            )
        )
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.setting,
                R.mipmap.nim_ic_me_setting
            ) {
                navigate(RoutePath.Nim.ACTIVITY_NIM_SETTING)
            }
        )
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_cache_clear,
                R.mipmap.nim_ic_me_cache_clear
            ) {
                navigate(RoutePath.Nim.ACTIVITY_NIM_ME_CLEAN_CACHE)
            }
        )
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_session_clear,
                R.mipmap.nim_ic_me_session_clear
            ) {
                navigate(RoutePath.Nim.ACTIVITY_NIM_ME_CLEAN_SESSION)
            }
        )
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_call,
                R.mipmap.nim_ic_me_call
            )
        )
        data.add(
            ItemViewModelMeGroup(
                this,
                R.string.me_about,
                R.mipmap.nim_ic_me_about
            )
        )
        it.value = data
    }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
        map<ItemViewModelMeGroup>(BR.item, R.layout.nim_item_me_group)
        map<ItemViewModelMeUser>(BR.item, R.layout.nim_item_me_user)
    }

    fun loadData() {
        AppStorage.account?.let { account ->
            useCaseGetUserInfo.execute(account)
                .autoLoading(this)
                .life(this)
                .subscribe {
                    userInfo.value = it.get()
                }
        }
    }

}