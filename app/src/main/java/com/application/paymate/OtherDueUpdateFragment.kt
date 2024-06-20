package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.FragmentOtherDueBinding

class OtherDueUpdateFragment : Fragment() {
    private lateinit var binding:FragmentOtherDueBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_other_due, container, false)

        val mateIdNode = sharedViewModel.mateNode.value.toString()

        val mateName = sharedViewModel.mateName.value.toString()
        val existingOtherAmount = sharedViewModel.otherAmount.value.toString()
        binding.updateOtherAmountEditText.setText(existingOtherAmount)

        //Setting up items of the Drop down spinner view
        val dropDownItems = arrayOf("Add", "Subtract")
        val dropDownView = binding.selectOptionDropDown
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dropDownItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDownView.adapter = adapter

        //Setting up logic for onItemSelected of the drop down.
        dropDownView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        binding.addIcon.setImageResource(R.drawable.baseline_add_24)

                        //Making an instance of the UpdateAmount class and calling the updateAmount method to add the amount in realtime database
                        val updateAmountObject = UpdateAmount(mateName)
                        view?.let {
                            updateAmountObject.updateAmount(
                                binding.updateChangesButton,
                                mateIdNode,
                                "update_other_amount",
                                "plus",
                                binding.enterOtherAmountEditText,
                                requireActivity(), it
                            )
                        }
                    }

                    1 -> {
                        binding.addIcon.setImageResource(R.drawable.baseline_minimize_24)

                        //Making an instance of the UpdateAmount class and calling the updateAmount method to add the amount in realtime database
                        val updateAmountObject = UpdateAmount(mateName)
                        view?.let {
                            updateAmountObject.updateAmount(
                                binding.updateChangesButton,
                                mateIdNode,
                                "update_other_amount",
                                "minus",
                                binding.enterOtherAmountEditText,
                                requireActivity(), it
                            )
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        return binding.root
    }
}