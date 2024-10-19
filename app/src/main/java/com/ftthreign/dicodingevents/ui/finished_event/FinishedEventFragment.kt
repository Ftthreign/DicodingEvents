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

        eventAdapter = EventAdapter(EventAdapter.VIEW_TYPE_FINISHED_AT_HOME)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishedEventViewModel.finishedEvent.observe(viewLifecycleOwner){event->
            eventAdapter.submitList(event)
        }

        finishedEventViewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            showLoading(isLoading)
        }

        finishedEventViewModel.errorMessage.observe(viewLifecycleOwner) { errMessage ->
            errMessage?.let {
                binding.apply {
                    errorMessage.text = it
                    errorMessage.visibility = View.VISIBLE
                    titleFinished.visibility = View.GONE
                    progressBar1.visibility = View.GONE
                    searchBar.visibility = View.GONE
                    searchView.visibility = View.GONE
                    finishedEvent.visibility = View.GONE
                }
            }

        }
    }

    private fun showLoading(isLoading : Boolean) {
        binding.progressBar1.visibility = if(isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}