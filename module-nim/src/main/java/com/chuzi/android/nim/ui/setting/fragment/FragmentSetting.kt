package com.chuzi.android.nim.ui.setting.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.databinding.NimFragmentSettingBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.nim.ui.setting.viewmodel.ItemViewModelSettingGroup
import com.chuzi.android.nim.ui.setting.viewmodel.ItemViewModelSettingLogout
import com.chuzi.android.nim.ui.setting.viewmodel.ItemViewModelSettingSwitch
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSetting
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage
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

    override fun initData() {
        super.initData()
        val data = ArrayList<Any>()
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingGroup(
                viewModel,
                R.string.setting_account_safe
            )
        )
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingGroup(
                viewModel,
                R.string.setting_font_size
            )
        )
        data.add(
            ItemViewModelSettingGroup(
                viewModel,
                R.string.setting_new_session
            )
        )
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingGroup(
                viewModel,
                R.string.settings_language_title
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_LANGUAGE)
            }
        )
        data.add(
            ItemViewModelSettingGroup(
                viewModel,
                R.string.settings_theme_title
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_THEME)
            }
        )
        data.add(
            ItemViewModelSettingSwitch(
                viewModel,
                R.string.settings_one_button_gray
            ) {
                val isGray = AppFactorySDK.isGrayImage.value != true
                AppStorage.isGray = isGray
                AppFactorySDK.isGrayImage.value = isGray
            }
        )
        data.add(ItemViewModelLine(viewModel))
        data.add(ItemViewModelSettingLogout(viewModel) {
            showLogoutDialog()
        })
        viewModel.items.value = data
    }

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
