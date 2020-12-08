package com.chuzi.android.nim.ui.event

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:47 PM
 * since: v 1.0.0
 */
class EventUI {

    class OnMessageDestoryEvent


    companion object {
        //录制取消
        const val RECORD_CANCEL = 0
        //录完可以发送
        const val RECORD_SEND = 1
        //正在录制中
        const val RECORD_RECORDING = 2
    }
    data class OnRecordAudioEvent(
        val recordType: Int,
        val recordSecond: Int
    )
    data class OnRecordCancelChangeEvent(
        val cancelled: Boolean
    )


}