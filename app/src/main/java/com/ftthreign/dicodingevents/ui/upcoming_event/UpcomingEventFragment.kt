package com.ftthreign.dicodingevents.ui.upcoming_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.ftthreign.dicodingevents.databinding.FragmentUpcomingEventBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter


class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter
    private val upcomingEventViewModel by viewModels<UpcomingEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        upcomingEventViewModel.upcomingEvent.observe(viewLifecycleOwner){event->
            adapter.submitList(event)
        }

        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            showLoading(isLoading)
        }

        upcomingEventViewModel.errorMessage.observe(viewLifecycleOwner){errorMessage ->
            if(errorMessage != null) {
                binding.errorMessage.text = errorMessage
                binding.errorMessage.visibility = View.VISIBLE
                binding.titleUpcoming.visibility = View.GONE
                binding.upcomingEvent.visibility = View.GONE
                binding.progressBar1.visibility = View.GONE
            }
        }

        adapter = EventAdapter()
        binding.upcomingEvent.layoutManager = GridLayoutManager(context, 2)
        binding.upcomingEvent.adapter = adapter

        upcomingEventViewModel.findUpcomingEvent()

        return root
    }

    private fun showLoading(isLoading : Boolean) {
        if(isLoading) {
            binding.progressBar1.visibility = View.VISIBLE
        } else {
            binding.progressBar1.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}