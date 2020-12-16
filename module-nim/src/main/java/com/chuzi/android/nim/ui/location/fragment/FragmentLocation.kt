package com.chuzi.android.nim.ui.location.fragment

import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.maps.model.animation.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.chuzi.android.libs.tool.dp2px
import com.chuzi.android.libs.tool.hideView
import com.chuzi.android.libs.tool.showView
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimFragmentLocationBinding
import com.chuzi.android.nim.ui.location.viewmodel.ViewModelLocation
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.ext.*
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.widget.log.lumberjack.L
import com.google.common.base.Optional
import com.rxjava.rxlife.life
import kotlin.math.sqrt

/**
 * desc 高德地图定位
 * author: 朱子楚
 * time: 2020/5/6 10:08 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Map.FRAGMENT_MAP_LOCATION)
class FragmentLocation : FragmentBase<NimFragmentLocationBinding, ViewModelLocation, ArgDefault>(),
    AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener {

    private lateinit var amap: AMap

    private var screenMarker: Marker? = null

    private var breatheMarker: Marker? = null

    private var isFirstLocation = true

    private var zoomLevel = 14f

    private var isOpenSearch = true

    private lateinit var geocodeSearch: GeocodeSearch

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_location

    override fun initView() {
        super.initView()

        geocodeSearch = GeocodeSearch(requireContext())

        initAmap()
        initLocation()

        binding.btnLocation.setOnClickListener {
            val location = amap.myLocation
            isOpenSearch = true
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude,
                        location.longitude
                    ), zoomLevel
                )
            )
        }

        amap.addOnMapTouchListener {
            if (it.action == MotionEvent.ACTION_UP) {
                isOpenSearch = true
            }
        }
    }

    /**
     * 初始化定位
     */
    private fun initLocation() {

    }

    /**
     * 初始化地图
     */
    private fun initAmap() {

        amap = binding.viewMap.map
        amap.setOnCameraChangeListener(this)

        setupLocationStyle()

        val uiSettings = amap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isMyLocationButtonEnabled = false

        amap.setOnMyLocationChangeListener(this)
        amap.isMyLocationEnabled = true

        amap.setOnMapLoadedListener {
            addMarkerInScreenCenter()
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onClickPoiItemEvent.observe(viewLifecycleOwner, {
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latLonPoint.latitude, it.latLonPoint.longitude),
                    zoomLevel
                )
            )
            viewModel.selectPoiItem(it)
        })

    }

    private fun setupLocationStyle() {
        // 自定义系统定位蓝点
        val locationStyle = MyLocationStyle()
        locationStyle.showMyLocation(false)
        amap.myLocationStyle =
            locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewMap.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.viewMap.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        binding.viewMap.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.viewMap.onResume()
    }

    override fun onDestroyView() {
        binding.viewMap.onDestroy()
        super.onDestroyView()
    }

    override fun onMyLocationChange(location: Location?) {
        if (location == null)
            return
        if (isFirstLocation) {
            amap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    zoomLevel
                )
            )
            isFirstLocation = false
        }
        startBreatheAnimation(location)
    }


    override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {
        if (cameraPosition == null)
            return
        zoomLevel = cameraPosition.zoom
        //屏幕中心的Marker跳动
        startJumpAnimation()
        val latLng: LatLng = amap.cameraPosition.target
        val latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        if (isOpenSearch) {
            searchLocation(latLonPoint)
            isOpenSearch = false
        }
    }

    override fun onCameraChange(cameraPosition: CameraPosition?) {

    }


    /**
     * 在屏幕中心添加一个Marker
     */
    private fun addMarkerInScreenCenter() {
        val latLng: LatLng = amap.cameraPosition.target
        val screenPosition: Point = amap.projection.toScreenLocation(latLng)
        screenMarker = amap.addMarker(
            MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.nim_purple_pin))
        )
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker?.setPositionByPixels(screenPosition.x, screenPosition.y)
    }

    /**
     * 显示Loading
     */
    private fun showBottomLoading() {
        showView(binding.loading)
    }

    /**
     * 隐藏Loading
     */
    private fun hideBottomLoading() {
        hideView(binding.loading)
    }


    /**
     * 屏幕中心marker 跳动
     */
    private fun startJumpAnimation() {
        if (screenMarker != null) { //根据屏幕距离计算需要移动的目标点
            val latLng = screenMarker!!.position
            val point: Point = amap.projection.toScreenLocation(latLng)
            point.y -= dp2px(requireContext(), 50f)
            val target: LatLng = amap.projection
                .fromScreenLocation(point)
            //使用TranslateAnimation,填写一个需要移动的目标点
            val animation: Animation = TranslateAnimation(target)
            animation.setInterpolator { input ->
                // 模拟重加速度的interpolator
                if (input <= 0.5) {
                    (0.5f - 2 * (0.5 - input) * (0.5 - input)).toFloat()
                } else {
                    (0.5f - sqrt((input - 0.5f) * (1.5f - input).toDouble())).toFloat()
                }
            }
            //整个移动所需要的时间
            animation.setDuration(600)
            //设置动画
            screenMarker?.setAnimation(animation)
            //开始动画
            screenMarker?.startAnimation()
        } else {
            L.tag("Amap").i {
                "screenMarker is null"
            }
        }
    }

    private fun startBreatheAnimation(location: Location) { // 呼吸动画
        if (breatheMarker == null) {
            val latLng = LatLng(location.latitude, location.longitude)
            breatheMarker = amap.addMarker(
                MarkerOptions().position(latLng)
                    .zIndex(1f)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.nim_gps_point))
            )
            // 中心的marker
            amap.addMarker(
                MarkerOptions().position(latLng)
                    .zIndex(2f)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.nim_gps_point))
            )
        }
        // 动画执行完成后，默认会保持到最后一帧的状态
        val animationSet = AnimationSet(true)
        val alphaAnimation = AlphaAnimation(0.5f, 0f)
        alphaAnimation.setDuration(2000)
        alphaAnimation.repeatCount = Animation.INFINITE
        val scaleAnimation = ScaleAnimation(1.0f, 3.5f, 1.0f, 3.5f)
        scaleAnimation.setDuration(2000)
        scaleAnimation.repeatCount = Animation.INFINITE
        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setInterpolator(LinearInterpolator())
        breatheMarker?.setAnimation(animationSet)
        breatheMarker?.startAnimation()
    }

    /**
     * 搜索周边
     */
    private fun searchLocation(latLonPoint: LatLonPoint) {
        createFlowable<Optional<RegeocodeAddress>> {
            val regeocodeQuery = RegeocodeQuery(latLonPoint, 1000f, GeocodeSearch.AMAP)
            onNextComplete(Optional.fromNullable(geocodeSearch.getFromLocation(regeocodeQuery)))
        }.bindToSchedulers().bindToException().doOnSubscribe {
            showBottomLoading()
        }.doFinally {
            hideBottomLoading()
        }.life(this).subscribe(
            {
                viewModel.updatePois(it.get(), latLonPoint)
            },
            {
                viewModel.handleThrowable(it)
            }
        )
    }


}