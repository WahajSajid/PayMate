package com.application.paymate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.application.paymate.databinding.PopupFragmentBinding

class LoadingDialogFragment : DialogFragment() {
    private lateinit var binding: PopupFragmentBinding
    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater,R.layout.popup_fragment, null,false)




        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        return dialog
    }

}