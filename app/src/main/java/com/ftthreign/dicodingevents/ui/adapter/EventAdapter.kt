package com.ftthreign.dicodingevents.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.databinding.LayoutItemBinding
import com.squareup.picasso.Picasso

class EventAdapter : ListAdapter<EventOverview, EventAdapter.ViewHolder>(
DIFF_CALLBACK) {
    inner class ViewHolder(private val binding : LayoutItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : EventOverview) {
            val (mediaCover, name) = data
            binding.dataTitle.text = name
            Picasso.get()
                .load(mediaCover)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventOverview>() {
            override fun areItemsTheSame(oldItem: EventOverview, newItem: EventOverview): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EventOverview,
                newItem: EventOverview
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}