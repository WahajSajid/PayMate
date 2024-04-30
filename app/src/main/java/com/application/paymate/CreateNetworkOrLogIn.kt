package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentCreateNetworkOrLogInBinding

@Suppress("DEPRECATION")
class CreateNetworkOrLogIn : Fragment() {
    lateinit var binding:FragmentCreateNetworkOrLogInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_network_or_log_in, container, false)
        binding.firstOptionCardView.setOnClickListener {
            binding.firstOptionCardView.setCardBackgroundColor(resources.getColor(R.color.hoverColor))
            binding.firstOptionText.setBackgroundColor(resources.getColor(R.color.hoverColor))
            view?.findNavController()?.navigate(R.id.action_createNetworkOrLogIn_to_adminInfo)
        }
        binding.secondOptionCardView.setOnClickListener {
            binding.secondOptionCardView.setCardBackgroundColor(resources.getColor(R.color.hoverColor))
            binding.secondOptionText.setBackgroundColor(resources.getColor(R.color.hoverColor))
            view?.findNavController()?.navigate(R.id.action_createNetworkOrLogIn_to_loginScreen)
        }


        return binding.root
    }
}