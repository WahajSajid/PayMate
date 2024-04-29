package com.application.paymate

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CNICValidator(private val view:EditText, private val errorMessage: String):TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        view.error = "CNIC number should be the length of 13"
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s.isNullOrBlank() || s.length<13){
            view.error = errorMessage
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }
}