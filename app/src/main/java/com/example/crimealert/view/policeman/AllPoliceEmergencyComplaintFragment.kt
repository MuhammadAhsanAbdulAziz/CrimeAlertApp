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
import com.example.crimealert.adapter.AdminEmergencyComplaintAdapter
import com.example.crimealert.databinding.FragmentAllPoliceEmergencyComplaintBinding
import com.example.crimealert.models.EmergencyComplaintResponse
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.EmergencyComplaintViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllPoliceEmergencyComplaintFragment : Fragment() {

    private var _binding : FragmentAllPoliceEmergencyComplaintBinding? = null
    private val binding get() = _binding!!
    private val emergencyComplaintViewModel by viewModels<EmergencyComplaintViewModel>()
    private lateinit var adminEmergencyComplaintAdapter: AdminEmergencyComplaintAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPoliceEmergencyComplaintBinding.inflate(inflater,container,false)

        adminEmergencyComplaintAdapter = AdminEmergencyComplaintAdapter(::onComplaintClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObersers()

        emergencyComplaintViewModel.getallcomplaints()

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_allPoliceEmergencyComplaintFragment_to_policeManageEmergencyComplaintFragment)
        }

        binding.noteList.layoutManager =LinearLayoutManager(requireContext())
        binding.noteList.adapter = adminEmergencyComplaintAdapter
    }


    private fun bindObersers() {
        emergencyComplaintViewModel.complaintResponseLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adminEmergencyComplaintAdapter.submitList(it.data)
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

    private fun onComplaintClicked(emergencyComplaintResponse: EmergencyComplaintResponse){
        val bundle = Bundle()
        bundle.putString("complaint", Gson().toJson(emergencyComplaintResponse))
        findNavController().navigate(R.id.action_allPoliceEmergencyComplaintFragment_to_policeManageEmergencyComplaintFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}