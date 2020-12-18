package com.chuzi.android.nim.ui.message.viewmodel

import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.domain.UseCaseDowloadAttachment
import com.chuzi.android.nim.domain.UseCaseGetImageAndVideoMessage
import com.chuzi.android.nim.tools.ToolImage
import com.chuzi.android.shared.tools.Weak
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.rxjava.rxlife.life

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
open class ItemViewModelMessageImage(
    val viewModel: ViewModelMessage,
    message: IMMessage
) : ItemViewModelMessageBase(viewModel, message) {

    /**
     * 下载附件UseCase
     */
    private val useCaseDowloadAttachment by lazy {
        UseCaseDowloadAttachment()
    }

    /**
     *
     */
    private val useCaseGetImageAndVideoMessage by lazy {
        UseCaseGetImageAndVideoMessage()
    }

    /**
     * ImageView 软引用
     */
    var imageRef by Weak<ImageView>()

    /**
     * 附件
     */
    var attachment = message.attachment as ImageAttachment

    /**
     * 图片Size
     */
    private val imageSize = ToolImage.getThumbnailDisplaySize(
        attachment.width.toFloat(),
        attachment.height.toFloat(),
        ToolImage.getImageMaxEdge().toFloat(),
        ToolImage.getImageMinEdge().toFloat()
    )

    /**
     * 图片高
     */
    val imageHeight = MutableLiveData<Int>().apply {
        value = imageSize.height
    }

    /**
     * 图片宽
     */
    val imageWidth = MutableLiveData<Int>().apply {
        value = imageSize.width
    }

    /**
     * 缩略图地址（下载到本地缩略图片）
     */
    val imageUrl = MutableLiveData<Any>().apply {
        value = getSdImagePath() ?: R.drawable.nim_bg_message_image
    }

    /**
     * 获取本地图片地址 原始图》缩略图
     */
    private fun getSdImagePath(): String? {
        val thumbPath = attachment.thumbPath
        val path = attachment.path
        return if (!path.isNullOrEmpty()) {
            path
        } else if (!thumbPath.isNullOrEmpty()) {
            thumbPath
        } else {
            null
        }
    }

    /**
     * 图片加载占位图
     */
    val imagePlaceholder = MutableLiveData(R.drawable.nim_bg_message_image)

    /**
     * 点击附加下载失败按钮，重新进行下载
     */
    val onClickAttachFailedCommand = createCommand {
        loadIamgeData()
    }

    /**
     * IM下载Image资源
     */
    private fun loadIamgeData() {
        useCaseDowloadAttachment.execute(
            UseCaseDowloadAttachment.Parameters(
                message, true,
                NimRequestCallback({

                })
            )
        )
    }

    /**
     * 点击图片跳转到图片详情
     */
    val onClickImageCommand = createTypeCommand<ImageView> {
        useCaseGetImageAndVideoMessage.execute(message).life(viewModel)
            .subscribe {

            }
    }

    /**
     * 判断是否去下载附件
     */
    fun checkDownLoadAttachment() {
        if (getSdImagePath() == null)
            loadIamgeData()
    }

}