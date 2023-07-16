package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.crimealert.R
import com.example.crimealert.databinding.FragmentProfileBinding
import com.example.crimealert.models.User
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getuser()
        binding.btnedit.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("user", Gson().toJson(user))
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment,bundle)
        }

        binding.btnComplaintCompleted.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("status","Completed")
            findNavController().navigate(R.id.action_profileFragment_to_complaintFragment2,bundle)
        }
        binding.btnComplaintPending.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("status","Pending")
            findNavController().navigate(R.id.action_profileFragment_to_complaintFragment2,bundle)
        }

        bindObersers()
    }

    private fun bindObersers() {
        userViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    user = it.data
                    binding.detail = it.data
                    if(user!!.RoleId == 3) {
                        binding.role.hint = "Civillian"
                    }
                    else if(user!!.RoleId == 2)
                    {
                        binding.role.hint = "Policeman"
                        binding.hehe1.isVisible = false
                        binding.hehe2.isVisible = false
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.error.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}