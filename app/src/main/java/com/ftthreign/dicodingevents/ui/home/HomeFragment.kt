package com.ftthreign.dicodingevents.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftthreign.dicodingevents.databinding.FragmentHomeBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var finishedAdapter: EventAdapter
    private lateinit var upcomingAdapter : EventAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.upcomingEvent.observe(viewLifecycleOwner) { events ->
            if(events != null) {
                upcomingAdapter.submitList(events)
                binding.errorMessage.visibility = View.GONE
            } else {
                binding.errorMessage.visibility = View.VISIBLE
            }
        }

        homeViewModel.finishedEvent.observe(viewLifecycleOwner) { events ->
            if(events != null) {
                finishedAdapter.submitList(events)
                binding.errorMessage.visibility = View.GONE
            } else {
                binding.errorMessage.visibility = View.VISIBLE
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            showLoading(isLoading)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner){errorMessage->
            if(errorMessage != null) {
                binding.errorMessage.text = errorMessage
                binding.errorMessage.visibility = View.VISIBLE
                binding.pageTitle.visibility = View.GONE
                binding.finishedEventTitle.visibility = View.GONE
                binding.dicodingEvent.visibility = View.GONE
                binding.upcomingEvent.visibility = View.GONE
                binding.finishedEvent.visibility = View.GONE
                binding.progressBar1.visibility = View.GONE
                binding.progressBar2.visibility = View.GONE
            }
        }

        setRv()

        // Finished Event
        homeViewModel.findEvent(0)
        // Upcoming Event
        homeViewModel.findEvent(1)
        return root
    }

    private fun setRv() {
        upcomingAdapter = EventAdapter()
        finishedAdapter = EventAdapter()
        binding.upcomingEvent.layoutManager = LinearLayoutManager(context)
        binding.finishedEvent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingEvent.adapter = upcomingAdapter
        binding.finishedEvent.adapter = finishedAdapter
    }


    private fun showLoading(isLoading : Boolean) {
        if(isLoading) {
            binding.progressBar1.visibility = View.VISIBLE
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar1.visibility = View.GONE
            binding.progressBar2.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}