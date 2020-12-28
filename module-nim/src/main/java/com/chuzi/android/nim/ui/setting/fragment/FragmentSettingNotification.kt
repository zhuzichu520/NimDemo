package com.chuzi.android.nim.ui.setting.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentSettingFontBinding
import com.chuzi.android.nim.databinding.NimFragmentSettingNotificationBinding
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSettingFont
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSettingNotification
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 消息通知设置
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_SETTING_NOTIFICATION)
class FragmentSettingNotification :
    FragmentBase<NimFragmentSettingNotificationBinding, ViewModelSettingNotification, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_setting_notification


}
