package com.application.paymate

import android.text.Editable
import android.text.TextWatcher

class PhoneNumberValidator(private val callBack: PhoneNumberValidatorCallBack):TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if((s.isNullOrBlank() || s.length<11)) callBack.onInputValidated(false)
        else callBack.onInputValidated(true)
        for (char in s.toString()) {
            if (char.isDigit()) {
                continue
            } else {
                callBack.onInputValidated(false)
                break
            }
        }
    }
    override fun afterTextChanged(s: Editable?) {

    }
}