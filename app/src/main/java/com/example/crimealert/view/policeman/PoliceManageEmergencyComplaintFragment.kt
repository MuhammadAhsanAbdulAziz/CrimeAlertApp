package com.example.crimealert.view.policeman

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentPoliceManageEmergencyComplaintBinding
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.Helper.errorMessage
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AnonymousViewModel
import com.example.crimealert.viewmodel.EmergencyComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoliceManageEmergencyComplaintFragment : Fragment() {

    private var _binding : FragmentPoliceManageEmergencyComplaintBinding? = null
    private val binding get() = _binding!!
    private val emergencycomplaintViewModel by viewModels<EmergencyComplaintViewModel>()
    private val anonymousViewModel by viewModels<AnonymousViewModel>()
    private var complaint : EmergencyComplaintResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoliceManageEmergencyComplaintBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(binding.addEditText.text == "Add Emergency Complaint")
                {
                    anonymousViewModel.addEmergencyComplaint(getComplaint())
                }
                else{
                    emergencycomplaintViewModel.updatestatuscomplaint(complaint!!.ECompId.toString(),getComplaint())
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
        val jsonNote = arguments?.getString("complaint")
        if(jsonNote != null){
            complaint = Gson().fromJson(jsonNote,EmergencyComplaintResponse::class.java)
            complaint?.let {
                binding.txtTitle.setText(it.ECompTitle)
                binding.txtDescription.setText(it.ECompDescription)
                if(it.ECompStatus == 1){
                    binding.unsolvedBtn.isChecked = true
                }
                else if(it.ECompStatus == 0){
                    binding.solvedBtn.isChecked = true
                }
            }
        }
        else{
            binding.addEditText.text = "Add Emergency Complaint"
            binding.txtDescription.isEnabled = true
            binding.txtTitle.isEnabled = true

        }
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val complaint = getComplaint()
        return Helper.validateEmergencyComplaintCredentials(complaint.ECompTitle,complaint.ECompDescription)
    }

    private fun getComplaint(): EmergencyComplaintRequest {
        val title = binding.txtTitle.text.toString()
        val des = binding.txtDescription.text.toString()
        var status = 0
        if(binding.unsolvedBtn.isChecked)
        {
            status = 1
        }

        return EmergencyComplaintRequest(des,status,title)
    }
    private fun bindObersers() {
        emergencycomplaintViewModel.statusLiveData.observe(viewLifecycleOwner) {
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
        ).setTitleText("IMPORTANT").setContentText("EmergencyComplaint Submitted Successfully").setConfirmText("Okay")
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