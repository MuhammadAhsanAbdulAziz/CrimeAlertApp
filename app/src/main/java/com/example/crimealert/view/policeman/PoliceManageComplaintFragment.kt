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
import com.example.crimealert.databinding.FragmentPoliceManageComplaintBinding
import com.example.crimealert.models.ComplaintRequest
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.models.UserResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.Helper.errorMessage
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.utils.UtilManager
import com.example.crimealert.viewmodel.ComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PoliceManageComplaintFragment : Fragment() {

    private var _binding : FragmentPoliceManageComplaintBinding? = null
    private val binding get() = _binding!!
    private val complaintViewModel by viewModels<ComplaintViewModel>()
    private var complaint : ComplaintResponse? = null
    @Inject
    lateinit var utilManager: UtilManager
    private var user: UserResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPoliceManageComplaintBinding.inflate(inflater,container,false)

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

        setInitialData()

        binding.btnSubmit.setOnClickListener {
            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(binding.addEditText.text == "Add Complaint")
                {
                    complaintViewModel.createcomplaint(getComplaint())
                }
                else{
                    complaintViewModel.updatestatuscomplaint(complaint!!.CompId.toString(),getComplaint())
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
            binding.addEditText.text = "Add Complaint"
            binding.txtName.isEnabled = true
            binding.txtPhone.isEnabled = true
            binding.txtDescription.isEnabled = true
            binding.txtTitle.isEnabled = true
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