package com.example.crimealert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crimealert.databinding.FeedbackItemBinding
import com.example.crimealert.models.FeedbackResponse


class AdminFeedbackAdapter(private val onFeedbackClicked: (FeedbackResponse) -> Unit) :
    ListAdapter<FeedbackResponse, AdminFeedbackAdapter.FeedbackViewHolder>(ComparatorDiffUtil()) {

    class ComparatorDiffUtil : DiffUtil.ItemCallback<FeedbackResponse>() {
        override fun areItemsTheSame(
            oldItem: FeedbackResponse,
            newItem: FeedbackResponse
        ): Boolean {
            return oldItem.FeedbackId == newItem.FeedbackId
        }

        override fun areContentsTheSame(
            oldItem: FeedbackResponse,
            newItem: FeedbackResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding =
            FeedbackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedbackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = getItem(position)
        feedback?.let {
            holder.bind(it)
        }
    }

    inner class FeedbackViewHolder(private val binding: FeedbackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feedback: FeedbackResponse) {
            binding.detail = feedback
            binding.rating.setText(""+feedback.FeedbackRating)
            binding.desc.setText(feedback.FeedbackDescription)
            binding.root.setOnClickListener {
                onFeedbackClicked(feedback)
            }

        }

    }
}
