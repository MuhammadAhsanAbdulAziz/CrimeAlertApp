package com.example.crimealert.view.civillian

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentCreateEmergencyComplaintBinding
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.Helper.errorMessage
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AnonymousViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class CreateEmergencyComplaintFragment : Fragment() {

    private var _binding : FragmentCreateEmergencyComplaintBinding? = null
    private val binding get() = _binding!!
    private val anonymousViewModel by viewModels<AnonymousViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEmergencyComplaintBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                anonymousViewModel.addEmergencyComplaint(getComplaint())
            }
            else{
                errorMessage(validationResult.second,requireContext())
            }

        }

        bindObersers()
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val complaint = getComplaint()
        return Helper.validateEmergencyComplaintCredentials(complaint.ECompTitle,complaint.ECompDescription)
    }

    private fun getComplaint(): EmergencyComplaintRequest {
        val title = binding.txtTitle.text.toString()
        val des = binding.txtDescription.text.toString()
        return EmergencyComplaintRequest(des,1,title)
    }
    private fun bindObersers() {
        anonymousViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    successMessage()
                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                }
            }
        }
    }

    private fun successMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.SUCCESS_TYPE
        ).setTitleText("IMPORTANT").setContentText("Emergency Complaint Submitted Successfully").setConfirmText("Okay")
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