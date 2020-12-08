package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.nim.domain.UseCaseGetMessageList
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.rxjava.rxlife.life

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */
class ViewModelMessage : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    private val useCaseGetMessageList by lazy {
        UseCaseGetMessageList()
    }

    val title = MutableLiveData("聊天")

    /**
     * 是否显示录音布局
     */
    val isShownRecord = MutableLiveData(false)

    /**
     * 录音时间显示
     */
    val recordTime = MutableLiveData<String>()

    /**
     * 录音手指移动tip的背景色
     */
    val tipAudioBackground = MutableLiveData<Int>()

    /**
     * 录音tip文本
     */
    val tipAudioText = MutableLiveData<Int>()

    /**
     * 加载消息数据
     *
     * @param anchor 查询开始消息
     * @param isFirst 是否第一次加载
     */
    fun loadData(anchor: IMMessage, isFirst: Boolean? = false) {
        useCaseGetMessageList.execute(UseCaseGetMessageList.Parameters(anchor, pageSize))
            .life(this)
            .subscribe(
                {

                },
                {
                    it.printStackTrace()
                    handleThrowable(it)
                }
            )
    }

}