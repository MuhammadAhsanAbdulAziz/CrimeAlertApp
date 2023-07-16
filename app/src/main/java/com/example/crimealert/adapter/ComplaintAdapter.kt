package com.example.crimealert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crimealert.databinding.ComplaintItemBinding
import com.example.crimealert.models.ComplaintResponse


class ComplaintAdapter :
    ListAdapter<ComplaintResponse, ComplaintAdapter.ComplaintViewHolder>(ComparatorDiffUtil()) {
    var counter = 1
    class ComparatorDiffUtil : DiffUtil.ItemCallback<ComplaintResponse>() {
        override fun areItemsTheSame(
            oldItem: ComplaintResponse,
            newItem: ComplaintResponse
        ): Boolean {
            return oldItem.CompId == newItem.CompId
        }

        override fun areContentsTheSame(
            oldItem: ComplaintResponse,
            newItem: ComplaintResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding =
            ComplaintItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = getItem(position)
        complaint?.let {
            holder.bind(it)
        }
    }

    inner class ComplaintViewHolder(private val binding: ComplaintItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(complaint: ComplaintResponse) {
            binding.detail = complaint
            binding.counter.text = counter.toString()
            counter++
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 80 //You can manage the blinking time with this parameter
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE

            if(complaint.CompStatus == 1) {
                binding.status.startAnimation(anim)
            }
        }

    }
}
