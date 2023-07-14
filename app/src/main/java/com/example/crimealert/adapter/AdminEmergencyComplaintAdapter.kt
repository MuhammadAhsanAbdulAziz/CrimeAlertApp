package com.example.crimealert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crimealert.databinding.EmergencyComplaintItemBinding
import com.example.crimealert.models.EmergencyComplaintResponse


class AdminEmergencyComplaintAdapter(private val onComplaintClicked: (EmergencyComplaintResponse) -> Unit) :
    ListAdapter<EmergencyComplaintResponse, AdminEmergencyComplaintAdapter.ComplaintViewHolder>(ComparatorDiffUtil()) {

    class ComparatorDiffUtil : DiffUtil.ItemCallback<EmergencyComplaintResponse>() {
        override fun areItemsTheSame(
            oldItem: EmergencyComplaintResponse,
            newItem: EmergencyComplaintResponse
        ): Boolean {
            return oldItem.ECompId == newItem.ECompId
        }

        override fun areContentsTheSame(
            oldItem: EmergencyComplaintResponse,
            newItem: EmergencyComplaintResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding =
            EmergencyComplaintItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = getItem(position)
        complaint?.let {
            holder.bind(it)
        }
    }

    inner class ComplaintViewHolder(private val binding: EmergencyComplaintItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(complaint: EmergencyComplaintResponse) {
            binding.detail = complaint
            binding.root.setOnClickListener {
                onComplaintClicked(complaint)
            }
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 80 //You can manage the blinking time with this parameter
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE

            binding.status.startAnimation(anim)

        }

    }
}
