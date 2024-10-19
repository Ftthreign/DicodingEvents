package com.ftthreign.dicodingevents.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ftthreign.dicodingevents.data.remote.response.EventOverview
import com.ftthreign.dicodingevents.databinding.LayoutFinishedHomeBinding
import com.ftthreign.dicodingevents.databinding.LayoutItemBinding
import com.ftthreign.dicodingevents.databinding.LayoutUpcomingHomeBinding
import com.ftthreign.dicodingevents.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

class EventAdapter(private val typeView : Int?) : ListAdapter<EventOverview, RecyclerView.ViewHolder>(
DIFF_CALLBACK) {
    inner class ViewHolder(private val binding : LayoutFinishedHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : EventOverview) {
            val (mediaCover, name) = data
            binding.dataTitle.text = name
            Picasso.get()
                .load(data.imageLogo)
                .into(binding.imgItemPhoto)
            binding.cityName.text = data.cityName
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(DetailActivity.EVENT_ID, data.id)

                val intent = Intent(binding.root.context, DetailActivity::class.java)
                intent.putExtras(bundle)
                binding.root.context.startActivity(intent)
            }
        }
    }

    inner class HomeUpcomingViewHolder(private val binding : LayoutUpcomingHomeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event : EventOverview){
            val (mediaCover, name, id, summary) = event
            binding.upcomingEventTitle.text = name
            binding.upcomingEventOverview.text = summary
            Picasso.get()
                .load(mediaCover)
                .into(binding.upcomingEventImage)
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(DetailActivity.EVENT_ID, id)

                val intent = Intent(binding.root.context, DetailActivity::class.java)
                intent.putExtras(bundle)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (typeView) {
            VIEW_TYPE_UPCOMING_AT_HOME -> {
                val binding = LayoutUpcomingHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomeUpcomingViewHolder(binding)
            }
            VIEW_TYPE_FINISHED_AT_HOME -> {
                val binding =
                    LayoutFinishedHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding)
            }
            else -> {
                val binding =
                    LayoutFinishedHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding)
            }
        }
    }




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        when (holder) {
            is HomeUpcomingViewHolder -> holder.bind(event)
            is ViewHolder -> holder.bind(event)
        }
    }


    companion object {
        const val VIEW_TYPE_UPCOMING_AT_HOME = 1
        const val VIEW_TYPE_FINISHED_AT_HOME = 2
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