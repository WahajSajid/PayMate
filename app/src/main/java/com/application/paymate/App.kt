package com.application.paymate

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
    var uid:String? = null
    var ids  = ArrayList<String>()
    var mateList = ArrayList<MatesInfo>()
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}