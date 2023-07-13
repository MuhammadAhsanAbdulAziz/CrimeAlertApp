package com.example.crimealert.view.civillian

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentCreateComplaintBinding
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.UserResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.utils.UtilManager
import com.example.crimealert.viewmodel.ComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class CreateComplaintFragment : Fragment() {

    private var _binding : FragmentCreateComplaintBinding? = null
    private val binding get() = _binding!!
    private val complaintViewModel by viewModels<ComplaintViewModel>()
    @Inject
    lateinit var utilManager: UtilManager
    private var user: UserResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateComplaintBinding.inflate(inflater,container,false)

        val jsonUser = utilManager.getUser()
        if (jsonUser != null) {
            user = Gson().fromJson(jsonUser, UserResponse::class.java)
            user?.let {
                binding.txtName.setText(it.user.FName + " " + it.user.LName)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                complaintViewModel.createcomplaint(getComplaint())
            }
            else{
                errorMessage(validationResult.second)
            }

        }

        bindObersers()
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val complaint = getComplaint()
        return Helper.validateComplaintCredentials(complaint.CompTitle,complaint.CompUPhone,complaint.CompDescription)
    }

    private fun getComplaint(): ComplaintRequest {
        val title = binding.txtTitle.text.toString()
        val phone = binding.txtPhone.text.toString()
        val des = binding.txtDescription.text.toString()
        return ComplaintRequest(des,title,phone,1)
    }
    private fun bindObersers() {
        complaintViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    SuccessMessage()
                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                }
            }
        })
    }

    fun SuccessMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.SUCCESS_TYPE
        ).setTitleText("IMPORTANT").setContentText("Complaint Submitted Successfully").setConfirmText("Okay")
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