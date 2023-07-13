package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentEditProfileBinding
import com.example.crimealert.models.User
import com.example.crimealert.models.UserRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        binding.btnEdit.setOnClickListener {
            val validationResult = validateUserInfo()
            if (validationResult.first) {
                userViewModel.updateuser(user!!.UserId, getUserRequest())
            } else {
                errorMessage(validationResult.second)
            }
        }
        bindObersers()
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("user")
        if (jsonNote != null) {
            user = Gson().fromJson(jsonNote, User::class.java)
            user?.let {
                binding.detail = it
            }
        } else {
//            binding.btnDelete.isVisible = false
//            binding.addEditText.text = "Add Note"
        }
    }

    private fun getUserRequest(): UserRequest {
        val fname = binding.txtFname.text.toString()
        val lname = binding.txtLname.text.toString()
        val cNIC = binding.txtCnic.text.toString()
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(fname, lname, "", cNIC, email, password, 3)
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return Helper.validateRegisterCredentials(
            userRequest.FName,
            userRequest.LName,
            userRequest.Email,
            userRequest.Password
        )
    }

    private fun bindObersers() {
        userViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    SuccessMessage()
                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                }
            }
        }
    }

    fun SuccessMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.SUCCESS_TYPE
        ).setTitleText("IMPORTANT").setContentText("Acount Updated Successfully").setConfirmText("Okay")
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

    fun errorMessage(msg: String?) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
            .setContentText(msg).show()
    }
}
