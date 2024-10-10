package com.ftthreign.dicodingevents.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.databinding.LayoutItemBinding
import com.ftthreign.dicodingevents.ui.detail.DetailActivity
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
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(DetailActivity.EVENT_ID, data.id)

                val intent = Intent(binding.root.context, DetailActivity::class.java)
                intent.putExtras(bundle)
                binding.root.context.startActivity(intent)
            }
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