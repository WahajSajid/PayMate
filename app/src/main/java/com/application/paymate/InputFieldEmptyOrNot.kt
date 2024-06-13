package com.application.paymate

import android.widget.EditText

class InputFieldEmptyOrNot {
    fun inputFieldEmptyOrNot(inputField:EditText):Boolean{
        return inputField.text.toString().isEmpty()
    }
}