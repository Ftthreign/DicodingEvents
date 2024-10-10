package com.ftthreign.dicodingevents.ui.finished_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftthreign.dicodingevents.databinding.FragmentFinishedEventBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter
    private val finishedEventViewModel by viewModels<FinishedEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        finishedEventViewModel.finishedEvent.observe(viewLifecycleOwner){event->
            eventAdapter.submitList(event)
        }

        finishedEventViewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            showLoading(isLoading)
        }

        finishedEventViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                binding.errorMessage.text = errorMessage
                binding.errorMessage.visibility = View.VISIBLE
                binding.titleFinished.visibility = View.GONE
                binding.progressBar1.visibility = View.GONE
                binding.searchBar.visibility = View.GONE
                binding.searchView.visibility = View.GONE
                binding.finishedEvent.visibility = View.GONE
            }
        }

        eventAdapter = EventAdapter()
        binding.finishedEvent.layoutManager = LinearLayoutManager(context)
        binding.finishedEvent.adapter = eventAdapter
        finishedEventViewModel.findFinishedEvent()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _,_,_ ->
                    val query = searchView.text.toString()
                    searchBar.setText(query)
                    searchView.hide()
                    finishedEventViewModel.findFinishedEvent(query)
                    false
                }
        }

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