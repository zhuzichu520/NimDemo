package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */
class ViewModelMessageOperation : ViewModelBase<ArgDefault>() {


    /**
     * 初始化数据
     */
    val items = MutableLiveData<List<ItemViewModelOperation>>().also {
        it.value = listOf(
            ItemViewModelOperation(
                this,
                R.string.message_operation_album,
                R.mipmap.nim_message_operation_album,
            ) {

            },
            ItemViewModelOperation(
                this,
                R.string.message_operation_video,
                R.mipmap.nim_message_operation_video,
            ) {

            },
            ItemViewModelOperation(
                this,
                R.string.message_operation_local,
                R.mipmap.nim_message_operation_local,
            ) {

            },
            ItemViewModelOperation(
                this,
                R.string.message_operation_file,
                R.mipmap.nim_message_operation_file,
            ) {

            },
            ItemViewModelOperation(
                this,
                R.string.message_operation_call,
                R.mipmap.nim_message_operation_call,
            ) {

            }
        )
    }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelOperation>(BR.item, R.layout.nim_item_operation)
    }


}