package com.chuzi.android.nim.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.tool.startActivity
import com.chuzi.android.libs.tool.toCast
import com.chuzi.android.mvvm.Mvvm
import com.chuzi.android.mvvm.ext.toPostcard
import com.chuzi.android.mvvm.ext.withArg
import com.chuzi.android.nim.R
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.route.RoutePath
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.msg.model.IMMessage


/**
 * desc 主界面
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.ACTIVITY_NIM_MAIN)
class ActivityNim : ActivityBase() {

    override fun getRoute(): String = RoutePath.Nim.FRAGMENT_NIM_MAIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            parseNotifyIntent(intent)
        }
    }

    /**
     * 再次回到主界面触发
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras ?: return
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            //有消息数据
            parseNotifyIntent(intent)
        } else {
            //有页面参数数据
            val arg = extras.getSerializable(Mvvm.KEY_ARG) as? ArgNim ?: return
            when (arg.mainType) {
                EnumNimType.LOGOUT -> {
                    navigate(RoutePath.Login.ACTIVITY_LOGIN_MAIN, isPop = true)
                }
                EnumNimType.RECREATE -> {
                    finish()
                    startActivity(
                        this,
                        ActivityNim::class.java,
                        bundle = null,
                        options = ActivityOptionsCompat.makeCustomAnimation(
                            this,
                            R.anim.fragment_fade_enter, R.anim.fragment_fade_exit
                        ).toBundle()
                    )
                }
                else -> {

                }
            }
        }
    }

    /**
     * 解析从通知栏进入App
     */
    private fun parseNotifyIntent(intent: Intent) {
        val messages: ArrayList<IMMessage>? =
            intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT)?.toCast()
        if (messages?.size == 1) {
            val message = messages[0]
            RoutePath.Nim.ACTIVITY_NIM_MESSAGE.toPostcard()
                .withArg(ArgMessage(message.sessionId, message.sessionType.value))
                .navigation()
        }
    }

}