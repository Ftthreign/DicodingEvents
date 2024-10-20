package com.ftthreign.dicodingevents.ui.favorite

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftthreign.dicodingevents.databinding.FragmentFavouriteBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter
import com.ftthreign.dicodingevents.ui.viewModels.MainViewModel
import com.ftthreign.dicodingevents.ui.viewModels.ViewModelFactory

class FavouriteFragment : Fragment() {
    private var _binding : FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : EventAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventAdapter(EventAdapter.VIEW_TYPE_UPCOMING_AT_HOME)
        binding.favouriteEvent.adapter = adapter
        binding.favouriteEvent.layoutManager = LinearLayoutManager(context)
        binding.favouriteEvent.setHasFixedSize(true)

        viewModel.getFavouriteEvent().observe(viewLifecycleOwner) {favEvent ->
            binding.progressBar1.visibility = View.GONE
            if(favEvent.isEmpty()) {
                binding.apply {
                    favouriteEvent.visibility = View.GONE
                    favouriteCount.visibility = View.GONE
                    emptyMessage.text = EMPTY_MESSAGE
                }
            }
            adapter.submitList(favEvent)
            binding.favouriteCount.text = "${ favEvent.size} event Favorit"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EMPTY_MESSAGE = "Anda belum menambahkan event ke Favorit, silahkan tambahkan Event ke Favorit"
    }

}