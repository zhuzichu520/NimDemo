package com.chuzi.android.nim.ui.location.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.RegeocodeAddress
import com.amap.api.services.geocoder.RegeocodeResult
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/6 10:09 PM
 * since: v 1.0.0
 */
class ViewModelLocation : ViewModelBase<ArgDefault>() {

    /**
     * 会话列表的会话数据
     */
    val pois = MutableLiveData<List<ItemViewModelPoi>>()

    /**
     * 会话列表所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map(pois) { input ->
            val list = ArrayList<Any>()
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelPoi>(BR.item, R.layout.nim_item_map_poi)
    }

    /**
     * 发送地图消息
     */
    val onSendCommand = createCommand {
        onClickPoiItemEvent.value?.let {
            RxBus.post(EventUI.OnSendLocationEvent(it))
        }
        back()
    }

    /**
     * PoiItem的点击事件
     */
    val onClickPoiItemEvent = SingleLiveEvent<ItemViewModelPoi>()

    /**
     * 刷新poi数据,默认选择第一项
     */
    fun updatePois(result: RegeocodeAddress, latLonPoint: LatLonPoint) {
        val list = result.pois
        if (list.isNullOrEmpty()) return
        pois.value = list.mapIndexed { index, poiItem ->
            val latLon = if (index == 0) latLonPoint else poiItem.latLonPoint
            ItemViewModelPoi(
                this,
                poiItem,
                result,
                index,
                latLon,
                onClickPoiItemEvent
            )
        }
        onClickPoiItemEvent.value = pois.value?.get(0)
    }

    fun selectPoiItem(itemViewModel: ItemViewModelPoi?) {
        if (itemViewModel == null)
            return
        pois.value?.let {
            pois.value = it.map { item ->
                item.apply {
                    showSelected.value = itemViewModel.index == index
                }
            }
        }
    }

}