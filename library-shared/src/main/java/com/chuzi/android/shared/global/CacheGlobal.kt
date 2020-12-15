package com.chuzi.android.shared.global

import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.chuzi.android.libs.tool.byteCountToDisplaySizeTwo
import com.chuzi.android.libs.tool.forceDelete
import com.chuzi.android.libs.tool.isExternalStorageWriteable
import com.chuzi.android.libs.tool.sizeOf
import com.chuzi.android.shared.global.AppGlobal.context
import java.io.File

object CacheGlobal {

    //缓存根根目录
    private const val CACHE_BASE_DISK = "cache_root"

    private const val CACHE_COIL_FILE_NAME = "cache_coil"

    private const val CACHE_GLIDE_FILE_NAME = "cache_glide"

    private const val CACHE_MMKV_FILE_NAME = "cache_mmkv"

    private const val CACHE_LOG_FILE_NAME = "cache_log"

    private const val CACHE_HTTP_FILE_NAME = "cache_http"

    private const val CACHE_COOKIE_FILE_NAME = "cache_cookie"

    private const val CACHE_NIM_FILE_NAME = "cache_nim"

    private const val CACHE_LUBAN_FILE_NAME = " cache_luban"



    fun initDir() {
        getCoilCacheDir()
        getMmkvCacheDir()
        getLogCacheDir()
        getCookieCacheDir()
        getHttpCacheDir()
        getLubanCacheDir()
    }

    /**
     * 获取Glide存放路径
     */
    fun getGlideCacheDir(): String {
        return getDiskCacheDir(CACHE_GLIDE_FILE_NAME).absolutePath
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
     * 获取压缩图片文件地址
     */
    fun getLubanCacheDir(): String {
        return getDiskCacheDir(CACHE_LUBAN_FILE_NAME).absolutePath
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
    fun getBaseDiskCacheDir(): File {
        val file = if (isExternalStorageWriteable()) {
            context.externalCacheDir ?: context.cacheDir
        } else {
            context.cacheDir
        }
        val baseFile = File(file.toString(), CACHE_BASE_DISK)
        if (!baseFile.exists()) {
            baseFile.mkdirs()
        }
        return baseFile
    }

    private fun getDiskCacheDir(last: String): File {
        val file = File(getBaseDiskCacheDir().toString(), last)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absoluteFile
    }

    /**
     * 清除所有缓存
     */
    fun clear() {
        forceDelete(getBaseDiskCacheDir())
    }

    private fun getCacheSize(): Long {
        return sizeOf(getBaseDiskCacheDir())
    }

    /**
     * 获取文件内容大小，保留两位小数
     */
    fun getCacheSizeString(): String {
        return byteCountToDisplaySizeTwo(getCacheSize())
    }

}