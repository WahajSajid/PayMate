package com.application.paymate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel(){

    //To store
    private var _adminName = MutableLiveData("")
    var adminName:MutableLiveData<String> = _adminName

}