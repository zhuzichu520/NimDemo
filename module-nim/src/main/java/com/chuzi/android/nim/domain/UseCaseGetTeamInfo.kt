package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.teamService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.google.common.base.Optional
import com.netease.nimlib.sdk.team.model.Team
import io.reactivex.rxjava3.core.Flowable

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/14 7:41 PM
 * since: v 1.0.0
 */
class UseCaseGetTeamInfo : UseCase<String, Flowable<Optional<Team>>>() {

    override fun execute(parameters: String): Flowable<Optional<Team>> {
        return createFlowable<Optional<Team>> {
            teamService().queryTeam(parameters).setCallback(NimRequestCallback(this))
        }.bindToSchedulers().bindToException()
    }

}