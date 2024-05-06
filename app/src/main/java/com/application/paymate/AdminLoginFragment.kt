package com.application.paymate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminRegistrationBinding
import com.application.paymate.databinding.FragmentLoginScreenBinding

class AdminLoginFragment : Fragment() {
    lateinit var binding: FragmentLoginScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login_screen, container, false)


        //Setting up click listener on Login Button
        binding.loginButton.setOnClickListener {
            val intent = Intent(context, AdminActivity::class.java)
            startActivity(intent)
        }
        binding.registerTextButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_adminLoginFragment_to_adminRegistrationFragment)
        }



        return binding.root
    }
}