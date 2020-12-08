package com.chuzi.android.shared.global

import com.chuzi.android.libs.tool.isExternalStorageWriteable
import com.chuzi.android.shared.global.AppGlobal.context
import java.io.File

object CacheGlobal {

    private const val CACHE_COIL_FILE_NAME = "cache_coil"

    private const val CACHE_MMKV_FILE_NAME = "cache_mmkv"

    private const val CACHE_LOG_FILE_NAME = "cache_log"

    private const val CACHE_HTTP_FILE_NAME = "cache_http"

    private const val CACHE_COOKIE_FILE_NAME = "cache_cookie"

    private const val CACHE_NIM_FILE_NAME = "cache_nim"

    fun initDir() {
        getCoilCacheDir()
        getMmkvCacheDir()
        getLogCacheDir()
        getCookieCacheDir()
        getHttpCacheDir()
    }

    /**
     * 获取Nim存放路径
     */
    fun getNimCacheDir(): String {
        return getDiskCacheDir(CACHE_NIM_FILE_NAME).absolutePath
    }

    /**
     * 获取日志存储路径
     */
    fun getLogCacheDir(): String {
        return getDiskCacheDir(CACHE_LOG_FILE_NAME).absolutePath
    }

    /**
     * 获取mmkv存储路径
     */
    fun getMmkvCacheDir(): String {
        return getDiskCacheDir(CACHE_MMKV_FILE_NAME).absolutePath
    }

    /**
     * 获取图片Coil缓存路径
     */
    fun getCoilCacheDir(): String {
        return getDiskCacheDir(CACHE_COIL_FILE_NAME).absolutePath
    }

    /**
     * 获取Cookiel缓存路径
     */
    fun getCookieCacheDir(): String {
        return getDiskCacheDir(CACHE_COOKIE_FILE_NAME).absolutePath
    }

    /**
     * 获取Http缓存路径
     */
    fun getHttpCacheDir(): String {
        return getDiskCacheDir(CACHE_HTTP_FILE_NAME).absolutePath
    }

    /**
     * 获取存储目录
     */
    private fun getBaseDiskCacheDir(): File {
        return if (isExternalStorageWriteable()) {
            context.externalCacheDir ?: context.cacheDir
        } else {
            context.cacheDir
        }
    }

    private fun getDiskCacheDir(last: String): File {
        val file = File(getBaseDiskCacheDir().toString(), last)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absoluteFile
    }

}