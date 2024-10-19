package com.ftthreign.dicodingevents.ui.upcoming_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingEventViewModel.upcomingEvent.observe(viewLifecycleOwner){event->
            adapter.submitList(event)
        }

        upcomingEventViewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            showLoading(isLoading)
        }

        upcomingEventViewModel.errorMessage.observe(viewLifecycleOwner){errMessage ->
            errMessage?.let {
                binding.apply {
                    errorMessage.text = it
                    errorMessage.visibility = View.VISIBLE
                    titleUpcoming.visibility = View.GONE
                    upcomingEvent.visibility = View.GONE
                    progressBar1.visibility = View.GONE
                }
            }
        }

        adapter = EventAdapter(EventAdapter.VIEW_TYPE_UPCOMING_AT_HOME)
        binding.upcomingEvent.layoutManager = LinearLayoutManager(context)
        binding.upcomingEvent.adapter = adapter

        upcomingEventViewModel.findUpcomingEvent()
    }


    private fun showLoading(isLoading : Boolean) {
        binding.progressBar1.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}