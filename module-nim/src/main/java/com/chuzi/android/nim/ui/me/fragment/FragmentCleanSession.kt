package com.chuzi.android.nim.ui.me.fragment

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentCleanSessionBinding
import com.chuzi.android.nim.ui.me.viewmodel.ViewModelCleanSession
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.databinding.view.setOnClickDoubleListener
import com.chuzi.android.shared.route.RoutePath
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_ME_CLEAN_SESSION)
class FragmentCleanSession :
    FragmentBase<NimFragmentCleanSessionBinding, ViewModelCleanSession, ArgDefault>(),
    View.OnClickListener {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_clean_session

    override fun initListener() {
        super.initListener()
        setOnClickDoubleListener(this, binding.delete)
    }

    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadData()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.delete -> {
                showDeleteDialog()
            }
        }
    }

    private fun showDeleteDialog() {
        QMUIDialog.MessageDialogBuilder(requireContext())
            .setMessage(R.string.delete_session_cache_tips)
            .setSkinManager(QMUISkinManager.defaultInstance(context))
            .addAction(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .addAction(0, R.string.ok, QMUIDialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                dialog.dismiss()
                viewModel.cleanSession()
            }.create(R.style.MyTheme_QMUI_Dialog).show()
    }

}