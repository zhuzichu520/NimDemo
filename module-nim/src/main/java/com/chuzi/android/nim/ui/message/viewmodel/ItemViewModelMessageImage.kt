package com.chuzi.android.nim.ui.message.viewmodel

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.domain.UseCaseDowloadAttachment
import com.chuzi.android.nim.domain.UseCaseGetImageAndVideoMessage
import com.chuzi.android.nim.tools.ToolImage
import com.chuzi.android.shared.tools.Weak
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.rxjava.rxlife.life

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
open class ItemViewModelMessageImage(
    viewModel: ViewModelMessage,
    message: IMMessage
) : ItemViewModelMessageBase(viewModel, message) {

    private val useCaseDowloadAttachment by lazy {
        UseCaseDowloadAttachment()
    }

    private val useCaseGetImageAndVideoMessage by lazy {
        UseCaseGetImageAndVideoMessage()
    }

    var photoView by Weak<ImageView>()

    var attachment = (message.attachment as ImageAttachment)

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
        val thumbPath = attachment.thumbPath
        val path = attachment.path
        value = if (!path.isNullOrEmpty()) {
            path
        } else if (!thumbPath.isNullOrEmpty()) {
            thumbPath
        } else {
            if (message.attachStatus == AttachStatusEnum.transferred || message.attachStatus == AttachStatusEnum.def) {
                useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, true))
                    .life(viewModel).subscribe { }
            }
            R.drawable.nim_bg_message_image
        }
    }

    val imagePlaceholder = MutableLiveData(R.drawable.nim_bg_message_image)

    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, true))
            .life(viewModel).subscribe { }
    }

    val onClickImageCommand = createTypeCommand<ImageView> {
        useCaseGetImageAndVideoMessage.execute(message).life(viewModel)
            .subscribe {

            }
    }


}