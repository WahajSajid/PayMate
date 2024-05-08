package com.application.paymate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
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
        //Update UI with admin name
        binding.adminName.text = sharedViewModel.adminName.value

        return binding.root
    }

}