package com.chuzi.android.nim.ui.location.viewmodel

import androidx.lifecycle.MutableLiveData
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.RegeocodeAddress
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/8 9:18 PM
 * since: v 1.0.0
 */
data class ItemViewModelPoi(
    val viewModel: BaseViewModel<*>,
    val poiItem: PoiItem,
    val regeocodeAddress: RegeocodeAddress,
    val index: Int,
    val latLonPoint: LatLonPoint,
    val onClickPoiItemListener: SingleLiveEvent<ItemViewModelPoi>
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<String>().apply {
        value = poiItem.title
    }

    val info = MutableLiveData<String>().apply {
        value = "${poiItem.distance}米 | ${regeocodeAddress.district}${poiItem.snippet}"
    }

    val showSelected = MutableLiveData<Boolean>()

    val onClickItemCommand = createCommand {
        onClickPoiItemListener.value = this
    }


}