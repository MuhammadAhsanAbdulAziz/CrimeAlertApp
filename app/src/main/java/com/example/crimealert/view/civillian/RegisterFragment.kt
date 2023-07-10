package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.crimealert.databinding.FragmentRegisterBinding
import com.example.crimealert.models.UserRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                authViewModel.registerUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }

        }
        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObersers()
    }

    private fun getUserRequest(): UserRequest {
        val Fname = binding.txtFname.text.toString()
        val Lname = binding.txtLname.text.toString()
        val CNIC = binding.txtCnic.text.toString()
        val Email = binding.txtEmail.text.toString()
        val Password = binding.txtPassword.text.toString()
        return UserRequest(Fname,Lname,"",CNIC,Email,Password,3)
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return Helper.validateRegisterCredentials(userRequest.FName,userRequest.LName,userRequest.Email,userRequest.Password)
    }

    private fun bindObersers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.error
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}