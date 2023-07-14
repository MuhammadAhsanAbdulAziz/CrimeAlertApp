package com.example.crimealert.view.civillian

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crimealert.adapter.ComplaintAdapter
import com.example.crimealert.databinding.FragmentComplaintBinding
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.ComplaintViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComplaintFragment : Fragment() {

    private var _binding : FragmentComplaintBinding? = null
    private val binding get() = _binding!!
    private val complaintViewModel by viewModels<ComplaintViewModel>()
    private lateinit var complaintAdapter: ComplaintAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintBinding.inflate(inflater,container,false)

        complaintAdapter = ComplaintAdapter()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObersers()

        val status = arguments?.getString("status")
        if(status!=null)
        {
            if (status == "Completed") {
                binding.textView.text = "Completed Complaints"
                complaintViewModel.getCompletedcomplaints()
            }
            else {
                binding.textView.text = "Pending Complaints"
                complaintViewModel.getPendingcomplaints()
            }
        }

        binding.noteList.layoutManager =LinearLayoutManager(requireContext())
        binding.noteList.adapter = complaintAdapter
    }


    private fun bindObersers() {
        complaintViewModel.complaintResponseLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    complaintAdapter.submitList(it.data)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}