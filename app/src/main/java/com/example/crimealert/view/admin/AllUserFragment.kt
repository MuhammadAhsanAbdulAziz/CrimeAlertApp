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
import com.example.crimealert.adapter.AdminComplaintAdapter
import com.example.crimealert.adapter.AdminUserAdapter
import com.example.crimealert.databinding.FragmentAllUserBinding
import com.example.crimealert.models.User
import com.example.crimealert.utils.NetworkResult
import com.example.crimealert.viewmodel.UserViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllUserFragment : Fragment() {

    private var _binding : FragmentAllUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var adminuserAdapter: AdminUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllUserBinding.inflate(inflater,container,false)

        adminuserAdapter = AdminUserAdapter(::onUserClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObersers()

        userViewModel.getAlluser()

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_allUserFragment_to_adminManageUserFragment)
        }

        binding.noteList.layoutManager =LinearLayoutManager(requireContext())
        binding.noteList.adapter = adminuserAdapter
    }


    private fun bindObersers() {
        userViewModel.alluserResponseLiveData.observe(viewLifecycleOwner){
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    adminuserAdapter.submitList(it.data)
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

    private fun onUserClicked(user: User){
        val bundle = Bundle()
        bundle.putString("user", Gson().toJson(user))
        findNavController().navigate(R.id.action_allUserFragment_to_adminManageUserFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}