package com.chuzi.android.shared.storage

import androidx.appcompat.app.AppCompatDelegate
import com.netease.nimlib.sdk.auth.LoginInfo
import com.tencent.mmkv.MMKV

/**
 * desc MMKV存储
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
object AppStorage {

    private const val PREFS_NAME = "app"

    private val prefs: Lazy<MMKV> = lazy {
        MMKV.mmkvWithID(PREFS_NAME)
    }

    /**
     * 账号
     */
    var account by StringPreference(prefs, null)

    /**
     * 密钥token
     */
    var token by StringPreference(prefs, null)

    /**
     * IM通知 默认开启
     */
    var notifyToggle by BooleanPreference(prefs, true)

    /**
     * uiModel 兼容Android 10
     */
    var uiMode by IntPreference(prefs, defaultValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    /**
     * 国际化语言
     */
    var language by StringPreference(prefs, defaultValue = null)

    /**
     * 字体大小等级
     */
    var fontLevel by IntPreference(prefs, defaultValue = 1)

    /**
     * 所有图片是否变灰（场景：国家出大事）
     */
    var isGray by BooleanPreference(prefs, false)

    /**
     * 是否开启私有化 todo
     */
    var isPrivateConfig by BooleanPreference(prefs, false)

    /**
     * 私有化配置Json数据 todo
     */
    var privateConfigJson by StringPreference(prefs, null)

    /**
     * 登入
     */
    fun login(loginInfo: LoginInfo) {
        account = loginInfo.account
        token = loginInfo.token
    }

    /**
     * 登出
     */
    fun logout() {
        account = null
        token = null
    }

}