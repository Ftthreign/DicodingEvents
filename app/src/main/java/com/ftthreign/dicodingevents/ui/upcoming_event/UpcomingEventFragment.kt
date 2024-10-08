package com.ftthreign.dicodingevents.ui.upcoming_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ftthreign.dicodingevents.databinding.FragmentUpcomingEventBinding

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val upcomingEventViewModel =
            ViewModelProvider(this).get(UpcomingEventViewModel::class.java)

        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.titleUpcoming
        upcomingEventViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}