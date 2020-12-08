package com.chuzi.android.shared.base

import android.os.Bundle
import com.chuzi.android.mvvm.base.BaseActivity
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.chuzi.android.shared.skin.SkinManager

abstract class ActivityBase : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val skinManager = QMUISkinManager .defaultInstance(this)
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
