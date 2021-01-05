package com.chuzi.android.nim.push

import android.app.Activity
import android.net.Uri
import android.os.Bundle


class MixPushActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        finish()
    }

    private fun parseIntent() {
        val intent = intent ?: return
        val uri: Uri? = intent.data
        val bundle: Bundle? = intent.extras
        val map: Map<String, String>
        if (uri != null) {
            val parameterSet: Set<String> = uri.queryParameterNames
            map = HashMap((parameterSet.size shl 2) / 3 + 1)
            var value: String?
            for (p in parameterSet) {
                value = uri.getQueryParameter(p)
                if (value == null) {
                    continue
                }
                map[p] = value
            }
        } else if (bundle != null) {
            map = HashMap((bundle.size() shl 2) / 3 + 1)
            for (key in bundle.keySet()) {
                val valueObj: Any? = bundle.get(key)
                map[key] = valueObj.toString()
            }
        } else {
            map = HashMap(0)
        }
        NimMixPushMessageHandler().onNotificationClicked(this.applicationContext, map)
    }


}
