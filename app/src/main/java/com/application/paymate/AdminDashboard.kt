package com.application.paymate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.application.paymate.databinding.FragmentAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminDashboard : Fragment() {
    private lateinit var binding: FragmentAdminDashboardBinding
    private lateinit var fragmentManager:FragmentManager
    private lateinit var loadingData:LoadingDialogFragment

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_dashboard, container, false)
        //Setting up shared preferences to update UI for admin name
        fragmentManager = childFragmentManager
        loadingData = LoadingDialogFragment()
        loadingData.show(fragmentManager,"loading")
        loadingData.isCancelable = false
        getName()

        //Setting up onClick Listener for all mates card and on all the views present in this card view. Because user can click on anything.
        binding.allMatesCardImage.setOnClickListener {
            val intent = Intent(requireContext(), AllMatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.allMatesCard.setOnClickListener{
            val intent = Intent(requireContext(), AllMatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.arrow1NextImageview.setOnClickListener{
            val intent = Intent(requireContext(), AllMatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        //Setting up onClick Listener for split dues card and on all the views present in this card view. Because user can click on anything.
        binding.splitDuesCardImage.setOnClickListener {
            val intent = Intent(requireContext(), SplitDuesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.splitDuesCard.setOnClickListener{
            val intent = Intent(requireContext(), SplitDuesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.arrow2NextImageview.setOnClickListener {
            val intent = Intent(requireContext(), SplitDuesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

            return binding.root
    }


   private fun getName(){
        val app = requireActivity().application as App
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        databaseReference.child("uid").get().addOnCompleteListener{ task ->
            if(task.isSuccessful) app.uid = task.result.value.toString()

        }

        databaseReference.child("name").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                binding.adminName.text = snapshot.value.toString()
                loadingData.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireActivity(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}