package com.application.paymate

import android.app.Application

class App: Application() {
    var uid:String? = null
    var ids  = ArrayList<String>()
    var mateList = ArrayList<MatesInfo>()
}