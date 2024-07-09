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

class SplitDuesAdapter(private val matesList: ArrayList<MatesInfo>,private val  context: Context,private val app: App) : RecyclerView.Adapter<SplitDuesAdapter.ViewHolder>() {

   private var mateIds = ArrayList<String>()
    private val checkBoxStates = SparseBooleanArray()
    private var selectAllButtonClicked:Boolean = false
    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    fun selectAllMates(selectAll:Boolean):ArrayList<String>{
        selectAllButtonClicked = selectAll
        matesList.forEach {it.isSelected  = selectAll}
        notifyDataSetChanged()
        return mateIds
    }

   inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)!!
        val id = itemView.findViewById<TextView>(R.id.mateIdForSplitBills)!!
        fun bind(item: MatesInfo) {
            checkBox.isChecked = item.isSelected
        }
        var clicked:Boolean = false
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


        // Set checkbox state from checkBoxStates array


        if(!selectAllButtonClicked){
            holder.checkBox.isChecked = checkBoxStates.get(position, false)
            holder.checkBox.setOnClickListener {
                holder.clicked = !holder.clicked
                if(holder.clicked){
                    app.ids.add(holder.id.text.toString())
                    checkBoxStates.put(position, holder.clicked)
                    holder.checkBox.isChecked = holder.clicked
                    Toast.makeText(context,app.ids.size.toString(),Toast.LENGTH_SHORT).show()
                }else{
                    if(selectAllButtonClicked) holder.checkBox.isChecked = !holder.clicked
                    app.ids.remove(holder.id.text.toString())
                    holder.checkBox.isChecked = holder.clicked
                    checkBoxStates.remove(position,!holder.clicked)
                    notifyItemChanged(position)
                    Toast.makeText(context,app.ids.size.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}