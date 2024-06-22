package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.FragmentSplitRentBinding

class SplitRentFragment : Fragment() {
    private lateinit var binding:FragmentSplitRentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_split_rent, container, false)
        return binding.root
    }

}