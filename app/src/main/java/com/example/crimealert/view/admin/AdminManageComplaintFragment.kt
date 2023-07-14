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
import com.example.crimealert.databinding.FragmentAdminManageComplaintBinding
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.Helper.errorMessage
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.ComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminManageComplaintFragment : Fragment() {

    private var _binding : FragmentAdminManageComplaintBinding? = null
    private val binding get() = _binding!!
    private val complaintViewModel by viewModels<ComplaintViewModel>()
    private var complaint : ComplaintResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageComplaintBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()

        binding.btnDelete.setOnClickListener{
            warningMessage()

        }
        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(binding.addEditText.text == "Add Complaint")
                {
                    complaintViewModel.createcomplaint(getComplaint())
                }
                else{
                    complaintViewModel.updatecomplaint(complaint!!.CompId.toString(),getComplaint())
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
            complaint = Gson().fromJson(jsonNote,ComplaintResponse::class.java)
            complaint?.let {
                binding.txtTitle.setText(it.CompTitle)
                binding.txtDescription.setText(it.CompDescription)
                binding.txtPhone.setText(it.CompUPhone)
                binding.txtName.setText(it.CompUName)
                if(it.CompStatus == 1){
                    binding.unsolvedBtn.isChecked = true
                }
                else if(it.CompStatus == 0){
                    binding.solvedBtn.isChecked = true
                }
            }
        }
        else{
            binding.btnDelete.isVisible = false
            binding.addEditText.text = "Add Complaint"
            binding.txtName.isEnabled = true
            binding.txtPhone.isEnabled = true
        }
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val complaint = getComplaint()
        return Helper.validateComplaintCredentials(complaint.CompTitle,complaint.CompUPhone,complaint.CompDescription)
    }

    private fun getComplaint(): ComplaintRequest {
        val title = binding.txtTitle.text.toString()
        val phone = binding.txtPhone.text.toString()
        val des = binding.txtDescription.text.toString()
        var status = 0
        if(binding.unsolvedBtn.isChecked)
        {
            status = 1
        }

        return ComplaintRequest(des,title,phone,status)
    }
    private fun bindObersers() {
        complaintViewModel.statusLiveData.observe(viewLifecycleOwner) {
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

    private fun warningMessage() {
        val alertDialog = SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.WARNING_TYPE
        ).setTitleText("IMPORTANT").setContentText("Are you Sure?").setConfirmText("Okay")
            .setConfirmClickListener { sweetAlertDialog ->
                complaintViewModel.deletecomplaint(complaint!!.CompId.toString())
                findNavController().popBackStack()
                sweetAlertDialog.cancel()
                sweetAlertDialog.dismiss()
            }
        alertDialog.show()
    }

    private fun successMessage() {
        val alertDialog = SweetAlertDialog(
            context,
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
}