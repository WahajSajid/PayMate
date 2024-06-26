package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class SplitDuesAdapter(private val matesList: ArrayList<MatesInfo>,private val  context: Context) : RecyclerView.Adapter<SplitDuesAdapter.ViewHolder>() {

   private var mateIds = ArrayList<String>()
    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {

        fun checkBoxClickListener(id: TextView,checkBox: CheckBox)
        val mutex: Mutex
    }

    fun itemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }


    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    fun selectAllMates(selectAll:Boolean):ArrayList<String>{
        matesList.forEach {it.isSelected  = selectAll}
        notifyDataSetChanged()
        return mateIds
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)!!
        val id = itemView.findViewById<TextView>(R.id.mateIdForSplitBills)!!

        fun bind(item: MatesInfo) {
            checkBox.isChecked = item.isSelected
        }

        init {
            checkBox.setOnClickListener {
                checkBox.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.checkBoxClickListener(id,checkBox)
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
        holder.id.text = matesNames.mate_id.toString()
        holder.bind(matesList[position])
        mateIds.add(holder.id.text.toString())
    }
}