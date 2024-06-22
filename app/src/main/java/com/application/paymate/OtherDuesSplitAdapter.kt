package com.application.paymate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class OtherDuesSplitAdapter(private val matesList: ArrayList<MatesInfo>,context: Context) : RecyclerView.Adapter<OtherDuesSplitAdapter.ViewHolder>() {


    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {

        fun checkBoxClickListener(checkBox: CheckBox)
        val mutex: Mutex
    }

    fun itemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }


    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

        init {
            checkBox.setOnClickListener {
                checkBox.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.checkBoxClickListener(checkBox)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_items, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return matesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matesNames = matesList[position]
        holder.checkBox.text = matesNames.name.toString()
    }
}