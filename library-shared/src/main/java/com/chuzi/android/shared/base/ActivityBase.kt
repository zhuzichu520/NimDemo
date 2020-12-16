package com.chuzi.android.shared.base

import android.os.Bundle
import com.chuzi.android.mvvm.base.BaseActivity
import com.chuzi.android.shared.ext.updateApplicationLanguage
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.shared.storage.AppStorage

abstract class ActivityBase : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        updateApplicationLanguage(AppStorage.language)
        super.onCreate(savedInstanceState)
        val skinManager = QMUISkinManager.defaultInstance(this)
        setSkinManager(skinManager)
        SkinManager.onSkinChangeListener.observe(this) {
            if (it == SkinManager.SKIN_DARK) {
                QMUIStatusBarHelper.setStatusBarDarkMode(this)
            } else {
                QMUIStatusBarHelper.setStatusBarLightMode(this)
            }
        }
    }

}
