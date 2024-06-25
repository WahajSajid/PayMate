package com.application.paymate

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class UpdateOrSplitDues(private var mateName: String) {

    fun updateDues(
        button: Button,
        mateIdNode: String,
        updateContext: String,
        updateDomain: String,
        editText: EditText,
        context: Context,
        view: View
    ) {
        button.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                if (NetworkUtil.isNetworkAvailable(context)) {
                    val amount = editText.text.toString()
                    val addAmount = UpdateAmount(mateName)
                    addAmount.updateAmount(amount,mateIdNode,updateContext,updateDomain, true,context,view)
                } else Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show()
        }
    }

   fun splitDues(
        button: Button,
        updateContext: String,
        updateDomain: String,
        editText: EditText,
        context: Context,
        view: View,
        mateIds:ArrayList<String>
    ) {
        button.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                if(mateIds.isNotEmpty()) {
                    if (NetworkUtil.isNetworkAvailable(context)) {
                        val amount = editText.text.toString().toInt()
                        val dividedAmount:Int = amount / mateIds.size
                        val addAmount = UpdateAmount(mateName)
                        for(mateId in mateIds.withIndex()){
                            val id = mateId.value
                            val mateIdNode = "Mate $id"
                            addAmount.updateAmount(
                                dividedAmount.toString(),
                                mateIdNode,
                                updateContext,
                                updateDomain,
                                false,
                                context,
                                view
                            )
                        }
                    } else Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT)
                        .show()
                }else Toast.makeText(context,"Please select any mate first",Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show()
        }
    }

}