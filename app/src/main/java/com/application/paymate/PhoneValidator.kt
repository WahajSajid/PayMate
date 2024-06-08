package com.application.paymate

import android.text.Editable
import android.text.TextWatcher

class PhoneValidator(private var callBack:PhoneValidatorCallBck):TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s.isNullOrEmpty() || s.length<11) callBack.onInputValidate(false)
        else callBack.onInputValidate(true)
    }

    override fun afterTextChanged(s: Editable?) {

    }
}