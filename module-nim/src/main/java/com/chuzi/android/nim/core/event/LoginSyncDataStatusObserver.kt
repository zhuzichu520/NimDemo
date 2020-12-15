package com.chuzi.android.nim.core.event

import android.os.Handler
import com.chuzi.android.libs.tool.toLong
import com.chuzi.android.nim.ext.authServiceObserver
import com.chuzi.android.shared.global.AppGlobal
import com.chuzi.android.widget.log.lumberjack.L
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/4 4:18 PM
 * since: v 1.0.0
 */
object LoginSyncDataStatusObserver {

    private const val TIME_OUT_SECONDS: Int = 10

    private var syncStatus = LoginSyncStatus.NO_BEGIN

    private var timeoutRunnable: Runnable? = null

    private var uiHandler: Handler? = null

    /**
     * 监听
     */
    private val observers = mutableListOf<Observer<Void>>()

    private val loginSyncStatusObserver = Observer<LoginSyncStatus> { status ->
        syncStatus = status
        if (status == LoginSyncStatus.SYNC_COMPLETED) {
            onLoginSyncDataCompleted(false)
        }
    }

    private val syncTeamMemberObserver =
        Observer<Boolean> {

        }

    /**
     * 在App启动时向SDK注册登录后同步数据过程状态的通知
     * 调用时机：主进程Application onCreate中
     */
    fun registerLoginSyncDataStatus(register: Boolean) {
        authServiceObserver().observeLoginSyncDataStatus(loginSyncStatusObserver, register)
        authServiceObserver().observeLoginSyncTeamMembersCompleteResult(
            syncTeamMemberObserver,
            register
        )
    }

    /**
     * 监听登录后同步数据完成事件，缓存构建完成后自动取消监听
     * 调用时机：登录成功后
     *
     * @param observer 观察者
     * @return 返回true表示数据同步已经完成或者不进行同步，返回false表示正在同步数据
     */
    fun observeSyncDataCompletedEvent(observer: Observer<Void>): Boolean {
        if (syncStatus == LoginSyncStatus.NO_BEGIN || syncStatus == LoginSyncStatus.SYNC_COMPLETED) {
            /*
             * NO_BEGIN 如果登录后未开始同步数据，那么可能是自动登录的情况:
             * PUSH进程已经登录同步数据完成了，此时UI进程启动后并不知道，这里直接视为同步完成
             */
            return true
        }
        // 正在同步
        if (!observers.contains(observer)) {
            observers.add(observer)
        }
        // 超时定时器
        if (uiHandler == null) {
            uiHandler = Handler(AppGlobal.context.mainLooper)
        }
        if (timeoutRunnable == null) {
            timeoutRunnable = Runnable { // 如果超时还处于开始同步的状态，模拟结束
                if (syncStatus == LoginSyncStatus.BEGIN_SYNC) {
                    onLoginSyncDataCompleted(true)
                }
            }
        }
        timeoutRunnable?.let {
            uiHandler?.removeCallbacks(it)
            uiHandler?.postDelayed(it, toLong(TIME_OUT_SECONDS * 1000))
        }


        return false
    }

    /**
     * 登录同步数据完成处理
     */
    private fun onLoginSyncDataCompleted(timeout: Boolean) {
        L.e { "同步数据是否超时：${timeout}" }
        // 移除超时任务（有可能完成包到来的时候，超时任务都还没创建）
        timeoutRunnable?.let {
            uiHandler?.removeCallbacks(it)
        }
        // 通知上
        for (o in observers) {
            o.onEvent(null)
        }
        // 重置状态
        reset()
    }

    /**
     * 注销时清除状态&监听
     */
    private fun reset() {
        syncStatus = LoginSyncStatus.NO_BEGIN
        observers.clear()
    }
}