package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crimealert.R
import com.example.crimealert.databinding.FragmentMainBinding
import com.example.crimealert.models.UserResponse
import com.example.crimealert.utils.UtilManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var utilManager: UtilManager
    private var user: UserResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        if (utilManager.getToken() != null) {
            binding.haha.isVisible = true
            val jsonUser = utilManager.getUser()
            if (jsonUser != null) {
                user = Gson().fromJson(jsonUser, UserResponse::class.java)
                user?.let {
                    binding.haha.setText("Welcome! " + it.user.FName + " " + it.user.LName)
                }
            }
            binding.btnComplaint.isVisible = true
            binding.btnProfile.isVisible = true
            binding.btnLogout.isVisible = true
            binding.btnFeedback.isVisible = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
        binding.btnEmergencycomplaint.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_emergencyComplaintFragment)
        }
        binding.btnComplaint.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_complaintFragment)
        }
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }
        binding.btnFeedback.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_feedbackFragment)
        }
        binding.btnLogout.setOnClickListener {
            refreshCurrentFragment()
            utilManager.saveToken(null)
        }
    }

    private fun refreshCurrentFragment(){
        val fragmentId = findNavController().currentDestination?.id
        findNavController().popBackStack(fragmentId!!,true)
        findNavController().navigate(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}