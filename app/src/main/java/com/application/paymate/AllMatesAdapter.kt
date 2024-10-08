package com.application.paymate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AllMatesAdapter(private var list: ArrayList<MatesInfo>) :
    RecyclerView.Adapter<AllMatesAdapter.ViewHolder>() {

    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener {
        fun editButtonListener(
            position: Int,
            mateId: TextView,
            mateText: TextView,
            mateName: TextView,
            matePhone: TextView,
        )

        fun removeButtonListener(mateId: TextView, mateText: TextView, mateName: TextView)
        fun updateButtonClickListener(
            rentAmount: TextView,
            otherAmount: TextView,
            walletAmount: TextView,
            mateText: TextView,
            mateId: TextView,
            mateName: TextView,
        )

        val mutex: Mutex
    }

    fun itemClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    class ViewHolder(itemView: View, clickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val mateId: TextView = itemView.findViewById(R.id.mateId)
        val mateName: TextView = itemView.findViewById(R.id.mateName)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNumber)
        private val mateText: TextView = itemView.findViewById(R.id.mateText)
        private val editButton: Button = itemView.findViewById(R.id.editDetailsButton)
        private val removeButton: Button = itemView.findViewById(R.id.removeButton)
        val cardView: CardView = itemView.findViewById(R.id.itemCard)
        private val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val rentAmount: TextView = itemView.findViewById(R.id.rentAmount)
        val otherAmount: TextView = itemView.findViewById(R.id.otherAmount)
        val walletAmount: TextView = itemView.findViewById(R.id.walletAmount)
        val buttonCard: ConstraintLayout = itemView.findViewById(R.id.buttonLayout)
        fun collapseExpandedView() {
            buttonCard.visibility = View.GONE
        }

        init {
            editButton.setOnClickListener {
                editButton.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.mutex.withLock {
                        clickListener.editButtonListener(
                            adapterPosition,
                            mateId,
                            mateText,
                            mateName,
                            phoneNumber
                        )
                    }
                }
            }
        }

        init {
            removeButton.setOnClickListener {
                removeButton.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.mutex.withLock {
                        clickListener.removeButtonListener(
                            mateId,
                            mateText,
                            mateName,
                        )
                    }
                }
            }
        }

        init {
            updateButton.setOnClickListener {
                updateButton.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    clickListener.mutex.withLock {
                        clickListener.updateButtonClickListener(
                            rentAmount,
                            otherAmount,
                            walletAmount,
                            mateText,
                            mateId,
                            mateName
                        )
                    }
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.all_info_items, parent, false)
        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matesList = list[position]
        holder.mateId.text = matesList.mate_id
        holder.mateName.text = matesList.name
        holder.phoneNumber.text = matesList.phone
        holder.rentAmount.text = matesList.rent_amount
        holder.otherAmount.text = matesList.other_amount
        holder.walletAmount.text = matesList.wallet_amount


        holder.cardView.setOnClickListener {
            isAnyItemExpanded(position)
            val expand = ScaleAnimation(
                0f, 1.0f,
                0f, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f
            )
            expand.duration = 250


            val contract = ScaleAnimation(
                1.1f, 0f,
                1f, 1f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f
            )
            contract.duration = 250
            if (matesList.isExpanded) {
                holder.buttonCard.startAnimation(contract)
                holder.buttonCard.visibility = View.GONE
                matesList.isExpanded = false
            } else {

                holder.buttonCard.startAnimation(expand)
                holder.buttonCard.visibility = View.VISIBLE
                matesList.isExpanded = true
            }
        }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = list.indexOfFirst { it.isExpanded }

        if (temp >= 0 && temp != position) {
            list[temp].isExpanded = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {

        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapseExpandedView()
        } else {
            super.onBindViewHolder(holder, position, payloads)

        }
    }


}