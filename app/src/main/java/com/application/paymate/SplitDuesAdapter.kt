package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.remove
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class SplitDuesAdapter(
    private val matesList: ArrayList<MatesInfo>,
    private val context: Context,
    private val app: App
) : RecyclerView.Adapter<SplitDuesAdapter.ViewHolder>() {

    private var mateIds = ArrayList<String>()
    private val checkBoxStates = SparseBooleanArray()
    private var selectAllButtonClicked: Boolean = false

    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    fun selectAllMates(selectAll: Boolean): ArrayList<String> {
        selectAllButtonClicked = selectAll
        matesList.forEach { it.isSelected = selectAll }
        if (selectAll) {
            checkBoxStates.clear()
            for (i in 0 until matesList.size) {
                checkBoxStates.put(i, true)
            }
        } else {
            checkBoxStates.clear()
            for (i in 0 until matesList.size) {
                checkBoxStates.put(i, false)
            }
        }
        notifyDataSetChanged()
        return mateIds
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)!!
        val id = itemView.findViewById<TextView>(R.id.mateIdForSplitBills)!!
        fun bind(item: MatesInfo) {

            if (selectAllButtonClicked) {
                checkBox.isChecked = item.isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return matesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matesNames = matesList[position]
        holder.checkBox.text = matesNames.name.toString()
        holder.id.text = matesNames.mate_id.toString()
        holder.bind(matesList[position])
        mateIds.add(holder.id.text.toString())


        holder.checkBox.isChecked = checkBoxStates.get(position, false)

        // Set checkbox state from checkBoxStates array
        holder.checkBox.setOnClickListener {
            if (matesNames.isSelected) {
                app.ids.remove(holder.id.text.toString())
                matesNames.isSelected = !matesNames.isSelected
                checkBoxStates.put(position, matesNames.isSelected)
                holder.checkBox.isChecked = matesNames.isSelected
                notifyItemChanged(position)
                Toast.makeText(context, app.ids.size.toString(), Toast.LENGTH_SHORT).show()
            } else {
                app.ids.add(holder.id.text.toString())
                matesNames.isSelected = !matesNames.isSelected
                holder.checkBox.isChecked = matesNames.isSelected
                checkBoxStates.put(position, matesNames.isSelected)
                Toast.makeText(context, app.ids.size.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }
}