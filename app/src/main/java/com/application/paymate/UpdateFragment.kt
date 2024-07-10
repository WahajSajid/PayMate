package com.application.paymate

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update, container, false)

        sharedPreferences = requireContext().getSharedPreferences(
            "com.application.paymate",
            Context.MODE_PRIVATE
        )
       val enabledAsMate = sharedPreferences.getBoolean("as_mate_enabled",false)


        //Retrieving data from the sharedViewModel
        val rentAmount = sharedViewModel.rentAmount.value.toString()
        val otherAmount = sharedViewModel.otherAmount.value.toString()
        val walletAmount = sharedViewModel.walletAmount.value.toString()
        val addToAdmin = sharedViewModel.admin.value.toString()


        //Overriding the text of the Text View to update the UI
        binding.rentUpdateEditText.setText(rentAmount)
        binding.otherUpdateEditText.setText(otherAmount)
        binding.walletUpdateEditText.setText(walletAmount)



        //Setting up if statement to check if the click on update button of admin card
        if (sharedViewModel.adminUpdateButtonClicked.value.toString()  == "true") {
            binding.walletLayout.visibility = View.GONE
        } else binding.walletLayout.visibility = View.VISIBLE
        //Setting up click listeners for the edit texts
        binding.rentUpdateEditText.setOnClickListener {
            sharedViewModel.updateContext.value = "update_rent"
            sharedViewModel.rentAmount.value = rentAmount
            view?.findNavController()
                ?.navigate(R.id.action_updateFragment2_to_otherDueUpdateFragment2)
        }

        binding.otherUpdateEditText.setOnClickListener {
            sharedViewModel.updateContext.value = "update_other_amount"
            sharedViewModel.otherAmount.value = otherAmount
            view?.findNavController()
                ?.navigate(R.id.action_updateFragment2_to_otherDueUpdateFragment2)
        }

        binding.walletUpdateEditText.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_updateFragment2_to_walletUpdateFragment4)
        }

        return binding.root
    }
}