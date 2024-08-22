package com.application.paymate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel(){


    //To share the Mate Node
    private var _mateNode = MutableLiveData("")
    var mateNode:MutableLiveData<String> = _mateNode


    //mate name
    private var _mateName = MutableLiveData("")
    var mateName:MutableLiveData<String> = _mateName

    //mate phone number
    private var _matePhone = MutableLiveData("")
    var matePhone:MutableLiveData<String> = _matePhone

    //Rent Amount
    private var _rentAmount = MutableLiveData("")
    var rentAmount:MutableLiveData<String> = _rentAmount

    //Other Amount
    private var _otherAmount = MutableLiveData("")
    var otherAmount:MutableLiveData<String> = _otherAmount


    //Wallet Amount
    private var _walletAmount = MutableLiveData("")
    var walletAmount:MutableLiveData<String> = _walletAmount


    //Wallet Amount
    private var __mateId = MutableLiveData(1)
    var _mateId:MutableLiveData<Int> = __mateId

    //Add amount to admin
    private var _admin = MutableLiveData<String>()
    var admin = _admin

    //Update context
    private var _updateContext = MutableLiveData<String>()
    var updateContext = _updateContext

    //Update Button clicked for admin
    private var _adminUpdateButtonClicked = MutableLiveData<String>()
    var adminUpdateButtonClicked = _adminUpdateButtonClicked



    init{
        __mateId.value = 1
    }

}