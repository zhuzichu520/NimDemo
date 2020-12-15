package com.chuzi.android.nim.ui.setting.fragment

import androidx.appcompat.app.AppCompatDelegate.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentSettingLanguageBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.nim.ui.setting.viewmodel.ItemViewModelSettingLanguage
import com.chuzi.android.nim.ui.setting.viewmodel.ViewModelSettingLanguage
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.ext.updateApplicationLanguage
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage
import java.util.*
import kotlin.collections.ArrayList

/**
 * desc 设置页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_SETTING_LANGUAGE)
class FragmentSettingLanguage :
    FragmentBase<NimFragmentSettingLanguageBinding, ViewModelSettingLanguage, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_setting_language

    override fun initData() {
        super.initData()
        val data = ArrayList<Any>()
        data.add(ItemViewModelLine(viewModel))
        data.add(
            ItemViewModelSettingLanguage(
                viewModel,
                null,
                viewModel.localeLiveData
            )
        )
        data.add(
            ItemViewModelSettingLanguage(
                viewModel,
                Locale.CHINA.language,
                viewModel.localeLiveData
            )
        )
        data.add(
            ItemViewModelSettingLanguage(
                viewModel,
                Locale.ENGLISH.language,
                viewModel.localeLiveData
            )
        )
        viewModel.items.value = data
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.localeLiveData.observe(viewLifecycleOwner) {
            if (it == AppStorage.language)
                return@observe
            updateApplicationLanguage(AppStorage.language)
            AppStorage.language = it
            navigate(RoutePath.Nim.ACTIVITY_NIM_MAIN, ArgNim(EnumNimType.RECREATE))
        }
    }


}
