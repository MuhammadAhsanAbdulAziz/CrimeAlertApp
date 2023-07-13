package com.example.crimealert.view.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crimealert.R
import com.example.crimealert.adapter.AdminFeedbackAdapter
import com.example.crimealert.databinding.FragmentAllFeedbackBinding
import com.example.crimealert.models.FeedbackResponse
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.FeedbackViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllFeedbackFragment : Fragment() {

    private var _binding : FragmentAllFeedbackBinding? = null
    private val binding get() = _binding!!
    private val feedbacViewModel by viewModels<FeedbackViewModel>()
    private lateinit var adminfeedbackAdapter: AdminFeedbackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllFeedbackBinding.inflate(inflater,container,false)

        adminfeedbackAdapter = AdminFeedbackAdapter(::onFeedbackClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObersers()

        feedbacViewModel.getallfeedbacks()

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_allFeedbackFragment_to_adminManageFeedbackFragment)
        }

        binding.noteList.layoutManager =LinearLayoutManager(requireContext())
        binding.noteList.adapter = adminfeedbackAdapter
    }


    private fun bindObersers() {
        feedbacViewModel.feedbackResponseLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adminfeedbackAdapter.submitList(it.data)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.error.toString(),Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun onFeedbackClicked(feedbackResponse: FeedbackResponse){
        val bundle = Bundle()
        bundle.putString("feedback", Gson().toJson(feedbackResponse))
        findNavController().navigate(R.id.action_allFeedbackFragment_to_adminManageFeedbackFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}