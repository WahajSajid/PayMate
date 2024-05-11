package com.application.paymate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginShareViewModel: ViewModel(){
    private var _isLoggedIn =  MutableLiveData<Boolean>()
    var isLoggedIn = _isLoggedIn

}