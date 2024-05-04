package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.FragmentLoginScreenBinding

class AdminLoginFragment : Fragment() {
    lateinit var binding:FragmentLoginScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login_screen, container, false)
//        binding.loginButton.setOnClickListener {
//            val intent = Intent(context, AdminActivity::class.java)
//            startActivity(intent)
//        }
        return binding.root
    }
}