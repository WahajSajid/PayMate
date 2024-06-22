package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.FragmentSplitOtherDuesBinding

class SplitOtherDuesFragment : Fragment() {
    private lateinit var binding: FragmentSplitOtherDuesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_split_other_dues, container, false)
        return binding.root
    }

}