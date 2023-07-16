package com.example.crimealert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crimealert.databinding.UserItemBinding
import com.example.crimealert.models.User


class AdminUserAdapter(private val onUserClicked: (User) -> Unit) :
    ListAdapter<User, AdminUserAdapter.UserViewHolder>(ComparatorDiffUtil()) {

    var counter = 1

    class ComparatorDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.UserId == newItem.UserId
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let {
            holder.bind(it)
        }
    }

    inner class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {

            binding.detail = user
            when (user.RoleId) {
                3 -> {
                    binding.desc.hint = "Civillian"
                }
                2 -> {
                    binding.desc.hint = "Policeman"
                }
                else -> {
                    binding.desc.hint = "Admin"
                }
            }
            binding.counter.text = counter.toString()
            counter++
            binding.root.setOnClickListener {
                onUserClicked(user)
            }
        }

    }
}
