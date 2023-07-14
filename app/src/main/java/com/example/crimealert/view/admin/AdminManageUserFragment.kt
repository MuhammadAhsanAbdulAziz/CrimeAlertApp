package com.example.crimealert.view.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crimealert.databinding.FragmentAdminManageUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminManageUserFragment : Fragment() {

    private var _binding : FragmentAdminManageUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageUserBinding.inflate(inflater,container,false)


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}