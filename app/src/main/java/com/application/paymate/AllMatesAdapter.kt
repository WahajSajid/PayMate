package com.application.paymate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class AllMatesAdapter():RecyclerView.Adapter<AllMatesAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val mateId:TextView = itemView.findViewById(R.id.mateId)
        val mateName:TextView = itemView.findViewById(R.id.mateName)
        val phoneNumber:TextView = itemView.findViewById(R.id.phoneNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_info_items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int = 7

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(position){
            0 -> holder.mateId.text = "1"
            1 -> holder.mateId.text = "2"
            2 -> holder.mateId.text = "3"
            3 -> holder.mateId.text = "4"
            4 -> holder.mateId.text = "5"
            5 -> holder.mateId.text = "6"
            6 -> holder.mateId.text = "7"
        }

    }


}