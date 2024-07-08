package com.application.paymate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.application.paymate.databinding.PopupFragmentBinding

class SigningInDialog:DialogFragment() {
    private lateinit var binding:PopupFragmentBinding
    @SuppressLint("UseGetLayoutInflater", "SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.popup_fragment, null, false)

        binding.loadingTextView.text = "Logging In..."

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        return dialog
    }
}