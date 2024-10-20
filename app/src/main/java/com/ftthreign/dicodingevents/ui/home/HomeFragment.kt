package com.ftthreign.dicodingevents.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftthreign.dicodingevents.databinding.FragmentHomeBinding
import com.ftthreign.dicodingevents.helper.Result
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter
import com.ftthreign.dicodingevents.ui.viewModels.MainViewModel
import com.ftthreign.dicodingevents.ui.viewModels.ViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var finishedAdapter: EventAdapter
    private lateinit var upcomingAdapter : EventAdapter
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(requireActivity())
}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getUpcomingEvent().observe(viewLifecycleOwner) {event->
            when(event)  {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar1.visibility = View.GONE
                    finishedAdapter.submitList(event.data)
                }
                is Result.Error -> {
                    binding.apply {
                    errorMessage.text = event.error
                    errorMessage.visibility = View.VISIBLE
                    pageTitle.visibility = View.GONE
                    finishedEventTitle.visibility = View.GONE
                    dicodingEvent.visibility = View.GONE
                    upcomingEvent.visibility = View.GONE
                    finishedEvent.visibility = View.GONE
                    progressBar1.visibility = View.GONE
                    progressBar2.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.getFinishedEvent().observe(viewLifecycleOwner) {event->
            Log.d("eventData : ", event.toString())
            when(event)  {
                is Result.Loading -> {
                    binding.progressBar1.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar2.visibility = View.GONE
                    upcomingAdapter.submitList(event.data.take(5).shuffled())
                }
                is Result.Error -> {
                    binding.apply {
                        errorMessage.text = event.error
                        errorMessage.visibility = View.VISIBLE
                        pageTitle.visibility = View.GONE
                        finishedEventTitle.visibility = View.GONE
                        dicodingEvent.visibility = View.GONE
                        upcomingEvent.visibility = View.GONE
                        finishedEvent.visibility = View.GONE
                        progressBar1.visibility = View.GONE
                        progressBar2.visibility = View.GONE
                    }
                }
            }
        }

        setRv()
        return root
    }

    private fun setRv() {
        upcomingAdapter = EventAdapter(EventAdapter.VIEW_TYPE_FINISHED_AT_HOME)
        finishedAdapter = EventAdapter(EventAdapter.VIEW_TYPE_UPCOMING_AT_HOME)
        binding.finishedEvent.layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.upcomingEvent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.upcomingEvent.adapter = upcomingAdapter
        binding.finishedEvent.adapter = finishedAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}