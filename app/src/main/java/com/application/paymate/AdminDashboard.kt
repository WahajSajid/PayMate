package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.snapshots

class AdminDashboard : Fragment() {
    private lateinit var binding: FragmentAdminDashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_dashboard, container, false)
        //Initilize firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference()
        val name =
            reference.child("admin_profiles").child("WSsTjYKDQoPdxcC1Yy05GGA0rJ12").child("name")
                .get().addOnSuccessListener {
                    binding.progressBar.visibility = View.VISIBLE
                binding.adminName.text = it.value.toString()
            }.addOnFailureListener{
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
        binding.progressBar.visibility = View.GONE

        return binding.root
    }

}