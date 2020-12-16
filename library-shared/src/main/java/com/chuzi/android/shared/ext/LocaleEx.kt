package com.chuzi.android.shared.ext

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * todo 替换过期方法
 * 更新语言 updateConfiguration 虽然过期，但是好用，没BUG
 */
fun Context.updateApplicationLanguage(language: String?) {
    val resources = this.resources
    val dm = resources.displayMetrics
    val config = resources.configuration
    val newLocale = Locale(language ?: Locale.getDefault().language)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Locale.setDefault(newLocale)
        config.setLocale(newLocale)
        val localeList = LocaleList(newLocale)
        LocaleList.setDefault(localeList)
        config.setLocales(localeList)
    } else {
        config.setLocale(newLocale)
    }
    @Suppress("DEPRECATION")
    resources.updateConfiguration(config, dm)
}