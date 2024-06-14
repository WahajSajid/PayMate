package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminDashboardBinding

class AdminDashboard : Fragment() {
    private lateinit var binding: FragmentAdminDashboardBinding
    private val sharedViewModel :SharedViewModel by activityViewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_dashboard, container, false)
        //Setting up shared preferences to update UI for admin name
        val sharedPreferences = requireActivity().getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)
        val adminName = sharedPreferences.getString("adminName","Loading....")
        binding.adminName.text  = adminName

        binding.allMatesCardImage.setOnClickListener {
            val intent = Intent(requireContext(), AllMatesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

            return binding.root
    }

}