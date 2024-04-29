package com.application.paymate

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class UsernameValidator(
    private val callBack: UsernameValidatorCallBack,
    private val editText: EditText
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //Check weather the user enter admin in username or not. If yes proceed ahead otherwise throw error.
        val isValid = (s.isNullOrBlank() || s.contains("admin"))
        callBack.onInputValidated(isValid)
        //Check if the user enter space (" ") or Uppercase of any word in between the input, if yes throw error else proceed ahead.
        if (s.isNullOrBlank() || s.contains(" ", true) || s.toString()
                .any() { it.isUpperCase() }
        ) callBack.onInputValidated(false)
        //Implemented logic to allow user to only input underscore ('_') for separating words, else throw error on using any other special character.
        for (char in s.toString()) {
            if (char.isLetterOrDigit() || char == '_') {
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