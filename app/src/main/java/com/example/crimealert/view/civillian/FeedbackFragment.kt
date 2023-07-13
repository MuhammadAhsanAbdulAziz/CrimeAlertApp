package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.crimealert.databinding.FragmentFeedbackBinding
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.utils.Helper
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.utils.UtilManager
import com.example.crimealert.viewmodel.AnonymousViewModel
import com.example.crimealert.viewmodel.FeedbackViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackFragment : Fragment() {

    private var _binding : FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private val feedbackViewModel by viewModels<FeedbackViewModel>()
    private val anonymousViewModel by viewModels<AnonymousViewModel>()
    @Inject
    lateinit var utilManager: UtilManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {

            val validationResult = validateUserInfo()
            if(validationResult.first){
                if(utilManager.getToken()!=null) {
                    feedbackViewModel.createfeedback(getFeedback())
                }
                else{
                    anonymousViewModel.createfeedback(getFeedback())
                }
            }
            else{
                errorMessage(validationResult.second)
            }

        }
        bindObersers()
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
        anonymousViewModel.statusLiveData.observe(viewLifecycleOwner){
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
        feedbackViewModel.statusLiveData.observe(viewLifecycleOwner){
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
        ).setTitleText("IMPORTANT").setContentText("Feedback Submitted Successfully").setConfirmText("Okay")
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