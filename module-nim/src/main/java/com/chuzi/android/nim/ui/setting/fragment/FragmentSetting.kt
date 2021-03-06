package com.chuzi.android.nim.ui.setting.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.databinding.NimFragmentSettingBinding
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSetting
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.route.RoutePath
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 * desc 设置页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_SETTING)
class FragmentSetting : FragmentBase<NimFragmentSettingBinding, ViewModelSetting, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_setting

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.onClickLogoutEvent.observe(viewLifecycleOwner) {
            showLogoutDialog()
        }
    }

    /**
     * 弹出退出登录确认弹窗
     */
    private fun showLogoutDialog() {
        QMUIDialog.MessageDialogBuilder(requireContext())
            .setMessage(R.string.logout_tips)
            .setSkinManager(QMUISkinManager.defaultInstance(context))
            .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .addAction(0, R.string.ok, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                dialog.dismiss()
                AppFactorySDK.logout()
                navigate(RoutePath.Nim.ACTIVITY_NIM_MAIN, ArgNim(EnumNimType.LOGOUT))
            }.create(R.style.MyTheme_QMUI_Dialog).show()
    }


}
