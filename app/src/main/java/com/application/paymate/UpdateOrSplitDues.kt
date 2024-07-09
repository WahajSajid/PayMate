package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class UpdateOrSplitDues(private var mateName: String) {

    fun updateDues(
        addToMateAlso:String,
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
                    addAmount.updateAmount(addToMateAlso,amount,mateIdNode,updateContext,updateDomain, true,context,view)
                } else Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show()
        }
    }

   @SuppressLint("SetTextI18n")
   fun splitDues(
        splitButton: Button,
        selectAllButton:Button,
        updateContext: String,
        updateDomain: String,
        editText: EditText,
        context: Context,
        view: View,
        app:App,
        checkBox: CheckBox,
        enabled:Boolean
    ) {
       splitButton.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                if(app.ids.isNotEmpty()) {
                    if (NetworkUtil.isNetworkAvailable(context)) {
                        selectAllButton.text = "Select All"
                        val mates:Int
                        val addAmount = UpdateAmount(mateName)
                        //Checking if the As Mate option is enabled or not by check the visibility of admin card view
                        if(enabled){
                            mates = app.ids.size + 1
                            //Calling a function to add the dues to admin also
                            addAmountToAdmin(mates,addAmount,checkBox,editText,updateContext,updateDomain,view,context)
                        } else mates = app.ids.size
                        val amount = editText.text.toString().toInt()
                        val dividedAmount:Int = amount / mates
                        for(mateId in app.ids.withIndex()){
                            val id = mateId.value
                            val mateIdNode = "Mate: $id"
                            addAmount.updateAmount(
                                "false",
                                dividedAmount.toString(),
                                mateIdNode,
                                updateContext,
                                updateDomain,
                                false,
                                context,
                                view
                            )
                        }
                        Toast.makeText(context,"Amount divided among selected mates",Toast.LENGTH_SHORT).show()
                        app.ids.clear()
                    } else Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT)
                        .show()
                }else Toast.makeText(context,"Please select any mate first",Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addAmountToAdmin(mates:Int,addAmount: UpdateAmount,checkBox:CheckBox,editText: EditText,updateContext: String,updateDomain: String,view: View,context: Context){
        if(checkBox.isChecked){
            val amount = editText.text.toString().toInt()
            val dividedAmount:Int = amount / mates
            addAmount.updateAmount(
                "true",
                dividedAmount.toString(),
                " ",
                updateContext,
                updateDomain,
                false,
                context,
                view
            )
            checkBox.isChecked = false
        }
    }

}