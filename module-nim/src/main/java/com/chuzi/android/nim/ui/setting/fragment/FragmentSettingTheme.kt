package com.chuzi.android.nim.ui.setting.fragment

import androidx.appcompat.app.AppCompatDelegate.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentSettingThemeBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.nim.ui.setting.viewmodel.ItemViewModelSettingTheme
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSettingTheme
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.shared.storage.AppStorage

/**
 * desc 设置页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_SETTING_THEME)
class FragmentSettingTheme :
    FragmentBase<NimFragmentSettingThemeBinding, ViewModelSettingTheme, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_setting_theme

    override fun initData() {
        super.initData()
        val data = ArrayList<Any>()
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingTheme(
                viewModel,
                MODE_NIGHT_NO,
                viewModel.uiMode
            )
        )
        data.add(
            ItemViewModelSettingTheme(
                viewModel,
                MODE_NIGHT_YES,
                viewModel.uiMode
            )
        )
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingTheme(
                viewModel,
                MODE_NIGHT_FOLLOW_SYSTEM,
                viewModel.uiMode
            )
        )
        viewModel.items.value = data
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.uiMode.observe(viewLifecycleOwner) {
            if (it == AppStorage.uiMode)
                return@observe
            AppStorage.uiMode = it
            SkinManager.applyConfigurationChanged(requireContext().resources.configuration)
        }
    }


}
