package com.application.paymate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.application.paymate.databinding.RemoveMatePopupFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RemoveMatePopUp : DialogFragment() {
    private lateinit var binding: RemoveMatePopupFragmentBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.remove_mate_popup_fragment, null, false)

        //Setting up the text view with the name of the mate
        binding.mateNameTextView.text = sharedViewModel.mateName.value.toString()

        //Implemented On click listeners for both yes and cancel button to perform particular function
        binding.yesButton.setOnClickListener {
            deleteMate()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        return dialog
    }

    //Function to delete the mate from the database
    private fun deleteMate() {
        val mateName = sharedViewModel.mateName.value.toString()
        val mateId = sharedViewModel.mateNode.value.toString()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates").child(mateId)
        databaseReference.removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                dismiss()
                val navController = findNavController()
                navController.navigate(R.id.action_allMates_to_adminDashboard2)
                Toast.makeText(context,"$mateName Removed Successfully",Toast.LENGTH_SHORT).show()
            } else{
                dismiss()
                Toast.makeText(context,"Some Failure Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

}