package com.application.paymate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

class AllMatesAdapter(private var list:ArrayList<MatesInfo>,context: Context):RecyclerView.Adapter<AllMatesAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val mateId:TextView = itemView.findViewById(R.id.mateId)
        val mateName:TextView = itemView.findViewById(R.id.mateName)
        val phoneNumber:TextView = itemView.findViewById(R.id.phoneNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_info_items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matesList = list[position]
        holder.mateId.text = matesList.mate_id
        holder.mateName.text = matesList.name
        holder.phoneNumber.text = matesList.phone
    }


}