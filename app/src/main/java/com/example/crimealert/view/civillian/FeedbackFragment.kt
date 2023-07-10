package com.example.crimealert.view.civillian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.crimealert.databinding.FragmentFeedbackBinding
import com.example.crimealert.models.FeedbackRequest
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.AuthViewModel
import com.example.crimealert.viewmodel.FeedbackViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : Fragment() {

    private var _binding : FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private val feedbackViewModel by viewModels<FeedbackViewModel>()

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
            feedbackViewModel.createfeedback(getFeedback())
        }
        bindObersers()
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
                    Toast.makeText(requireContext(),it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is NetworkResult.Error -> {
                }

                is NetworkResult.Loading -> {
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}