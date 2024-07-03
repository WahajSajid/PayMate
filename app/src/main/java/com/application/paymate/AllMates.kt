package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.paymate.databinding.FragmentAllMatesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.sync.Mutex
import org.w3c.dom.Text

class AllMates : Fragment() {
    private lateinit var binding: FragmentAllMatesBinding
    private lateinit var matesList: ArrayList<MatesInfo>
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_mates, container, false)
        // Initialize Firebase
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
        // Initialize matesList
        matesList = ArrayList()
        // Show spinner
        binding.spinnerLayout.visibility = View.VISIBLE
        // Recycler View initialization and adapter setting
        val recyclerView = binding.allMatesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AllMatesAdapter(matesList, requireContext())
        recyclerView.adapter = adapter


        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            val showCard = ShowAdminCard()
            showCard.showAdminCard(binding.itemCard)
            retrieveAdminDuesData()
        }

        //Animation to show the button
        val showButton = ScaleAnimation(
            0f, 1.0f,
            0f, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f
        )
        showButton.duration = 250

        //Animation to hide the button
        val hideButton = ScaleAnimation(
            1.1f, 0f,
            1f, 1f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f
        )
        hideButton.duration = 250


        var isClickedOrNot = false
        //Setting up onClick Listener for admin Card
        binding.itemCard.setOnClickListener {
            if (isClickedOrNot) {
                binding.updateButton.startAnimation(hideButton)
                binding.updateButton.visibility = View.GONE
                isClickedOrNot = !isClickedOrNot
            } else {
                binding.updateButton.startAnimation(showButton)
                binding.updateButton.visibility = View.VISIBLE
                isClickedOrNot = !isClickedOrNot
            }
        }


        binding.updateButton.setOnClickListener {
            updateDues()
        }


        //Setting Up click listener for editButton on recycler view item
        adapter.itemClickListener(object : AllMatesAdapter.OnItemClickListener {
            override fun editButtonListener(
                position: Int,
                mateId: TextView,
                mateText: TextView,
                mateName: TextView,
                matePhone: TextView
            ) {
                sharedViewModel.mateNode.value = "Mate: " + mateId.text.toString()
                sharedViewModel.mateName.value = mateName.text.toString()
                sharedViewModel.matePhone.value = matePhone.text.toString()
                view?.findNavController()
                    ?.navigate(R.id.action_allMates2_to_editMateDetailsFragment)

            }

            override fun removeButtonListener(
                mateId: TextView,
                mateText: TextView,
                mateName: TextView
            ) {
                sharedViewModel.mateNode.value = "Mate: " + mateId.text.toString()
                sharedViewModel.mateName.value = mateName.text.toString()
                val popScreen = RemoveMatePopUp()
                popScreen.show(childFragmentManager, "remove_popup_fragment")
            }

            override fun updateButtonClickListener(
                rentAmount: TextView,
                otherAmount: TextView,
                walletAmount: TextView,
                mateText: TextView,
                mateId: TextView,
                mateName: TextView
            ) {
                sharedViewModel.rentAmount.value = rentAmount.text.toString()
                sharedViewModel.otherAmount.value = otherAmount.text.toString()
                sharedViewModel.walletAmount.value = walletAmount.text.toString()
                sharedViewModel.mateNode.value = "Mate: " + mateId.text.toString()
                sharedViewModel.mateName.value = mateName.text.toString()
                sharedViewModel.adminUpdateButtonClicked.value = "false"
                view?.findNavController()?.navigate(R.id.action_allMates2_to_updateFragment2)
            }


            override val mutex: Mutex = Mutex()
        })


        //Value Event listener to retrieve the data from firebase realtime database
        if (NetworkUtil.isNetworkAvailable(requireContext())) {

            databaseReference.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.spinnerLayout.visibility = View.GONE

                    //Checking if any mate is exists in the database or not. If exists then show the list is empty message
                    if (!snapshot.exists()) binding.emptyListLayout.visibility = View.VISIBLE
                    else binding.emptyListLayout.visibility = View.GONE

                    binding.spinnerLayout.visibility = View.GONE
                    matesList.clear()
                    for (data in snapshot.children) {
                        val mateInfo = data.getValue(MatesInfo::class.java)
                        matesList.add(mateInfo!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    binding.spinnerLayout.visibility = View.GONE
                }
            })

        } else {
            binding.spinnerLayout.visibility = View.GONE
            binding.noInternetConnectionIconLayout.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun showAdminCard() {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate").child("enabled")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    binding.itemCard.visibility = View.VISIBLE
                    retrieveAdminDuesData()
                } else binding.itemCard.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun updateDues() {
        //Navigating to update dues fragment.
        val rentAmount = binding.rentAmount.text.toString()
        val otherDuesAmount = binding.otherAmount.text.toString()
        sharedViewModel.rentAmount.value = rentAmount
        sharedViewModel.otherAmount.value = otherDuesAmount
        sharedViewModel.admin.value = "true"
        sharedViewModel.adminUpdateButtonClicked.value = "true"
        view?.findNavController()?.navigate(R.id.action_allMates2_to_updateFragment2)
    }

    private fun retrieveAdminDuesData() {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        valueEventListener(binding.rentAmount, databaseReference.child("rent_amount"))
        valueEventListener(binding.otherAmount,databaseReference.child("other_amount"))
    }

    private fun valueEventListener(textView: TextView, databaseReference: DatabaseReference) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                textView.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}