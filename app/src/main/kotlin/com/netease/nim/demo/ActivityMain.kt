package com.netease.nim.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chuzi.android.nim.api.AppFactorySDK

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppFactorySDK.startLoginActivity(this)
    }

}