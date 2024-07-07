package com.application.paymate

import android.widget.EditText
 object LoginInputsEmptyChecker {
    fun emptyOrNot(editText1:EditText,editText2: EditText):Boolean{
        val empty :Boolean = !(editText1.text.isEmpty() || editText2.text.isEmpty())
        return empty
    }
}