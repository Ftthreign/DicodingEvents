package com.ftthreign.dicodingevents.ui.finished_event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ftthreign.dicodingevents.databinding.FragmentFinishedEventBinding

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedEventViewModel =
            ViewModelProvider(this).get(FinishedEventViewModel::class.java)

        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        finishedEventViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}