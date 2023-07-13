package com.example.crimealert.view.admin

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
import com.example.crimealert.databinding.FragmentAdminManageFeedbackBinding
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.models.FeedbackResponse
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.utils.UtilManager
import com.example.crimealert.viewmodel.FeedbackViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class AdminManageFeedbackFragment : Fragment() {

    private var _binding : FragmentAdminManageFeedbackBinding? = null
    private val binding get() = _binding!!
    private val feedbackViewModel by viewModels<FeedbackViewModel>()
    @Inject
    lateinit var utilManager: UtilManager
    private var feedback : FeedbackResponse? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminManageFeedbackBinding.inflate(inflater,container,false)
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
                if(binding.addEditText.text == "Create Feedback")
                {
                    feedbackViewModel.createfeedback(getFeedback())
                }
                else{
                    feedbackViewModel.updatefeedback(feedback!!.FeedbackId.toString(),getFeedback())
                }

            }
            else{
                errorMessage(validationResult.second)
            }

        }

        bindObersers()
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("feedback")
        if(jsonNote != null){
            feedback = Gson().fromJson(jsonNote,FeedbackResponse::class.java)
            feedback?.let {
                binding.txtDescription.setText(it.FeedbackDescription)
                binding.Rating.rating = it.FeedbackRating.toFloat()
            }
        }
        else{
            binding.btnDelete.isVisible = false
            binding.addEditText.text = "Create Feedback"
        }
    }

    private fun validateUserInfo(): Pair<Boolean, String> {
        val feedback = getFeedback()
        return Helper.validatefeedbackCredentials(feedback.FeedbackDescription)
    }

    private fun getFeedback(): FeedbackRequest {
        val des = binding.txtDescription.text.toString()
        val rating = binding.Rating.rating

        return FeedbackRequest(des,rating.toInt())
    }
    private fun bindObersers() {
        feedbackViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
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
        ).setTitleText("IMPORTANT").setContentText("Feedback Submitted Successfully").setConfirmText("Okay")
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
                feedbackViewModel.deletefeedback(feedback!!.FeedbackId.toString())
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