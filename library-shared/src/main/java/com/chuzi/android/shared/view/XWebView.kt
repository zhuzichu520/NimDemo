package com.chuzi.android.shared.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebSettings
import com.chuzi.android.libs.tool.toStringEmpty
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedTopWebView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIPackageHelper
import com.qmuiteam.qmui.util.QMUIResHelper
import com.chuzi.android.shared.R

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/28 5:41 PM
 * since: v 1.0.0
 */
class XWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : QMUIContinuousNestedTopWebView(context, attrs, defStyleAttr) {

    init {
        initWebSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebSetting() {
        val webSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.defaultTextEncodingName = "GBK"
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.textZoom = 100
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        val screen = QMUIDisplayHelper.getScreenWidth(context)
            .toString() + "x" + QMUIDisplayHelper.getScreenHeight(context)
        val userAgent = ("QMUIDemo/" + QMUIPackageHelper.getAppVersion(context)
                + " (Android; " + Build.VERSION.SDK_INT
                + "; Screen/" + screen + "; Scale/" + QMUIDisplayHelper.getDensity(context) + ")")
        val agent = settings.userAgentString
        if (agent == null || !agent.contains(userAgent)) {
            settings.userAgentString = "$agent $userAgent"
        }
        // 开启调试
        setWebContentsDebuggingEnabled(true)
    }

    fun exec(jsCode: String?) {
        evaluateJavascript(jsCode.toStringEmpty(), null)
    }

    override fun getExtraInsetTop(density: Float): Int {
        return (QMUIResHelper.getAttrDimen(context, R.attr.qmui_topbar_height) / density).toInt()
    }

}