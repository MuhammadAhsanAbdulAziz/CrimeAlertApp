package com.example.crimealert.view.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentAdminManageUserBinding
import com.example.crimealert.models.User
import com.example.crimealert.models.UserRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.Helper.errorMessage
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AuthViewModel
import com.example.crimealert.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminManageUserFragment : Fragment() {

    private var _binding : FragmentAdminManageUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private var user : User? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageUserBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        binding.btnDelete.setOnClickListener{
            warningMessage()

        }
        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(binding.addEditText.text == "Add User")
                {
                    authViewModel.registerUser(getUserRequest())
                }
                else{
                    userViewModel.updateuserrole(user!!.UserId,getUserRequest())
                }

            }
            else{
                errorMessage(validationResult.second,requireContext())
            }

        }
        bindObersers()
    }

    @SuppressLint("SetTextI18n")
    private fun setInitialData() {
        val jsonNote = arguments?.getString("user")
        if(jsonNote != null){
            user = Gson().fromJson(jsonNote,User::class.java)
            user?.let {
                binding.detail = it
                when (it.RoleId) {
                    3 -> {
                        binding.CivillianBtn.isChecked = true
                    }
                    2 -> {
                        binding.PolicemanBtn.isChecked = true
                    }
                    else -> {
                        binding.AdminBtn.isChecked = true
                    }
                }
            }
        }
        else{
            binding.btnDelete.isVisible = false
            binding.addEditText.text = "Add User"
            binding.txtFname.isEnabled = true
            binding.txtLname.isEnabled = true
            binding.txtCnic.isEnabled = true
            binding.txtEmail.isEnabled = true
            binding.txtPassword.isEnabled = true
        }
    }

    private fun getUserRequest(): UserRequest {
        val fname = binding.txtFname.text.toString()
        val lname = binding.txtLname.text.toString()
        val cNIC = binding.txtCnic.text.toString()
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        var role = 3
        if(binding.PolicemanBtn.isChecked)
        {
            role = 2
        }
        else if(binding.AdminBtn.isChecked)
        {
            role = 1
        }

        return UserRequest(fname,lname,"",cNIC,email,password,role)
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return Helper.validateRegisterCredentials(userRequest.FName,userRequest.LName,userRequest.Email,userRequest.Password)
    }
    private fun bindObersers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    successMessage("User Created Successfully")
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.error
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
        userViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    successMessage("User Updated Successfully")
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.error
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }
    private fun warningMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE
        ).setTitleText("IMPORTANT").setContentText("Are you Sure?").setConfirmText("Okay")
            .setConfirmClickListener { sweetAlertDialog ->
                userViewModel.deleteuser(user!!.UserId)
                findNavController().popBackStack()
                sweetAlertDialog.cancel()
                sweetAlertDialog.dismiss()
            }
        alertDialog.show()
    }

    private fun successMessage(message:String) {
        val alertDialog = SweetAlertDialog(
            context,
            SweetAlertDialog.SUCCESS_TYPE
        ).setTitleText("IMPORTANT").setContentText(message).setConfirmText("Okay")
            .setConfirmClickListener { sweetAlertDialog ->
                findNavController().popBackStack()
                sweetAlertDialog.cancel()
                sweetAlertDialog.dismiss()
            }
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}