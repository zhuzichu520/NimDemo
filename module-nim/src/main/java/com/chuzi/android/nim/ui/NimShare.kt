package com.chuzi.android.nim.ui

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSession

object NimShare {

    /**
     * 最近会话livedata对象，在会话列表中维护该集合
     * @see com.chuzi.android.nim.ui.session.fragment.FragmentSession
     */
    val sessionLiveData = MutableLiveData<List<ItemViewModelSession>>()
}