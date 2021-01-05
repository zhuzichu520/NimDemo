package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.domain.UseCaseGetFriends
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSearch
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.diffEquals
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.global.AppGlobal.context
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.rxjava.rxlife.life
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc 我的好友ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelFriends : ViewModelBase<ArgDefault>() {

    private val useCaseGetFriends by lazy {
        UseCaseGetFriends()
    }

    private val itemFriends = MutableLiveData<List<Any>>()

    val items: LiveData<List<Any>> = Transformations.map(itemFriends) {
        val data = ArrayList<Any>()
        data.add(ItemViewModelSearch(this))
        data.addAll(it)
        data
    }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelFriend>(BR.item, R.layout.nim_item_friend)
        map<ItemViewModelFriendIndex>(BR.item, R.layout.nim_item_friend_index)
        map<ItemViewModelSearch>(BR.item, R.layout.nim_item_search)
    }

    /**
     * 字母文本
     */
    val letter = MutableLiveData<String>()

    /**
     * 是否显示字母
     */
    val showLetter = MutableLiveData(false)

    /**
     * 下载好友列表数据
     */
    fun loadFriends() {
        useCaseGetFriends.execute(Unit)
            .map {
                convertItemViewModel(it)
            }
            .life(this)
            .subscribe {
                itemFriends.value = it
            }
    }

    /**
     * 将数据转换为ItemViewModel
     */
    private fun convertItemViewModel(it: List<NimUserInfo>): List<Any> {
        val data = it.map { item ->
            ItemViewModelFriend(this, item)
        }
        val map = hashMapOf<String, ArrayList<ItemViewModelFriend>?>()
        val letters = context.resources.getStringArray(R.array.letter_list)
        letters.forEach { letter ->
            map[letter] = null
        }
        data.forEach { item ->
            var letter = "#"
            val list = if (Character.isUpperCase(item.pinYin)) {
                letter = item.pinYin.toString()
                map[letter] ?: arrayListOf()
            } else {
                map[letter] ?: arrayListOf()
            }
            list.add(item)
            map[letter] = list
        }
        val list = arrayListOf<Any>()
        map.forEach { entry ->
            entry.value?.let { value ->
                list.add(ItemViewModelFriendIndex(this, entry.key))
                value.forEach { item ->
                    list.add(item)
                }
            }
        }
        return list
    }

    /**
     * 根据字符获取下标
     */
    fun getIndexByLetter(letter: String?): Int? {
        val data = items.value ?: return null
        data.forEachIndexed { index, any ->
            if (any is ItemViewModelFriendIndex && any.letter == letter) {
                return index
            }
        }
        return null
    }


}
