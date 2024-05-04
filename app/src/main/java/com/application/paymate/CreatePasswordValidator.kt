package com.application.paymate

import android.text.Editable
import android.text.TextWatcher

class CreatePasswordValidator(private val callBack: CreaatePasswordValidatorCallBack):TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }


    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s.isNullOrBlank() || s.isEmpty()) callBack.onInputValidated(false)
        else callBack.onInputValidated(true)
    }


    override fun afterTextChanged(s: Editable?) {

    }
}