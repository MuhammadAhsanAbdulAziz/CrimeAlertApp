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
import com.example.crimealert.R
import com.example.crimealert.databinding.FragmentLoginBinding
import com.example.crimealert.models.UserRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.utils.UtilManager
import com.example.crimealert.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var utilManager: UtilManager
    @Inject
    lateinit var userManager: UtilManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                authViewModel.loginUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }

        }

        bindObersers()
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest("","","","",emailAddress,password,0)
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return Helper.validateCredentials(userRequest.Username,userRequest.Email,userRequest.Password,true)
    }

    private fun bindObersers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    utilManager.saveUser(it.data!!)
                    utilManager.saveToken(it.data.token)
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
