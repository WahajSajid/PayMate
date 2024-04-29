package com.application.paymate

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CNICValidator(private val callBack: CNICValidatorCallBack):TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if((s.isNullOrBlank() || s.length<13)){
            callBack.onInputValidated(false)
        }
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