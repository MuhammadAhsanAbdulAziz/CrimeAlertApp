package com.example.crimealert.view.policeman

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
import com.example.crimealert.adapter.AdminComplaintAdapter
import com.example.crimealert.databinding.FragmentAllPoliceComplaintBinding
import com.example.crimealert.models.ComplaintResponse
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.ComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPoliceComplaintFragment : Fragment() {

    private var _binding : FragmentAllPoliceComplaintBinding? = null
    private val binding get() = _binding!!
    private val complaintViewModel by viewModels<ComplaintViewModel>()
    private lateinit var admincomplaintAdapter: AdminComplaintAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPoliceComplaintBinding.inflate(inflater,container,false)

        admincomplaintAdapter = AdminComplaintAdapter(::onComplaintClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObersers()

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_allPoliceComplaintFragment_to_policeManageComplaintFragment)
        }

        complaintViewModel.getallcomplaints()

        binding.noteList.layoutManager =LinearLayoutManager(requireContext())
        binding.noteList.adapter = admincomplaintAdapter
    }


    private fun bindObersers() {
        complaintViewModel.complaintResponseLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    admincomplaintAdapter.submitList(it.data)
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

    private fun onComplaintClicked(complaintResponse: ComplaintResponse){
        val bundle = Bundle()
        bundle.putString("complaint", Gson().toJson(complaintResponse))
        findNavController().navigate(R.id.action_allPoliceComplaintFragment_to_policeManageComplaintFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}