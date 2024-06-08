package com.application.paymate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AllMatesAdapter(private var list:ArrayList<MatesInfo>,context: Context):RecyclerView.Adapter<AllMatesAdapter.ViewHolder>(){

    private lateinit var  clickListener: OnItemClickListener

    interface OnItemClickListener{
        fun itemClickListener(position: Int,mateId:TextView,mateText:TextView,mateName:TextView,matePhone:TextView)
        val mutex: Mutex
    }
    fun itemClickListener(listener: OnItemClickListener){
        clickListener = listener
    }

    class ViewHolder(itemView: View,clickListener:OnItemClickListener):RecyclerView.ViewHolder(itemView) {
        val mateId:TextView = itemView.findViewById(R.id.mateId)
        val mateName:TextView = itemView.findViewById(R.id.mateName)
        val phoneNumber:TextView = itemView.findViewById(R.id.phoneNumber)
        val mateText:TextView = itemView.findViewById(R.id.mateText)
        val editButton: Button = itemView.findViewById(R.id.ediDetailsButton)

        init {
            editButton.setOnClickListener {
                editButton.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.mutex.withLock { clickListener.itemClickListener(adapterPosition,mateId,mateText,mateName,phoneNumber) }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_info_items,parent,false)
        return ViewHolder(view,clickListener)
    }

    override fun getItemCount():Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matesList = list[position]
        holder.mateId.text = matesList.mate_id
        holder.mateName.text = matesList.name
        holder.phoneNumber.text = matesList.phone
    }


}