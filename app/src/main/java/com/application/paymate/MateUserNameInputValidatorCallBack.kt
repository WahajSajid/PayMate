package com.application.paymate

import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException

interface MateUserNameInputValidatorCallBack {
    fun onInputValidated(isValid:Boolean)
}