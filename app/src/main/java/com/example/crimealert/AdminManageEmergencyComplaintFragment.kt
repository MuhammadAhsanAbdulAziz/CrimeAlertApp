package com.example.crimealert

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentAdminManageComplaintBinding
import com.example.crimealert.databinding.FragmentAdminManageEmergencyComplaintBinding
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.models.EmergencyComplaintRequest
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AnonymousViewModel
import com.example.crimealert.viewmodel.ComplaintViewModel
import com.example.crimealert.viewmodel.EmergencyComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminManageEmergencyComplaintFragment : Fragment() {

    private var _binding : FragmentAdminManageEmergencyComplaintBinding? = null
    private val binding get() = _binding!!
    private val emergencycomplaintViewModel by viewModels<EmergencyComplaintViewModel>()
    private val anonymousViewModel by viewModels<AnonymousViewModel>()
    private var complaint : EmergencyComplaintResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageEmergencyComplaintBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        binding.btnDelete.setOnClickListener{
            WarningMessage()

        }
        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(binding.addEditText.text == "Add Emergency Complaint")
                {
                    anonymousViewModel.addEmergencyComplaint(getComplaint())
                }
                else{
                    emergencycomplaintViewModel.updatecomplaint(complaint!!.ECompId.toString(),getComplaint())
                }

            }
            else{
                errorMessage(validationResult.second)
            }

        }

        bindObersers()
    }

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
            binding.btnDelete.isVisible = false
            binding.addEditText.text = "Add Emergency Complaint"

        }
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val complaint = getComplaint()
        return Helper.validateEmergencyComplaintCredentials(complaint.ECompTitle,complaint.ECompDescription)
    }

    private fun getComplaint(): EmergencyComplaintRequest {
        val title = binding.txtTitle.text.toString()
        val des = binding.txtDescription.text.toString()
        var status:Int = 0
        if(binding.unsolvedBtn.isChecked)
        {
            status = 1
        }

        return EmergencyComplaintRequest(des,status,title)
    }
    private fun bindObersers() {
        anonymousViewModel.statusLiveData.observe(viewLifecycleOwner) {
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
        emergencycomplaintViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
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
        ).setTitleText("IMPORTANT").setContentText("EmergencyComplaint Submitted Successfully").setConfirmText("Okay")
            .setConfirmClickListener { sweetAlertDialog ->
                findNavController().popBackStack()
                sweetAlertDialog.cancel()
                sweetAlertDialog.dismiss()
            }
        alertDialog.show()
    }
    fun WarningMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE
        ).setTitleText("IMPORTANT").setContentText("Are you Sure?").setConfirmText("Okay")
            .setConfirmClickListener { sweetAlertDialog ->
                emergencycomplaintViewModel.deletecomplaint(complaint!!.ECompId.toString())
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