package com.ftthreign.dicodingevents.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.TextView
import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.data.response.EventsResponse
import com.ftthreign.dicodingevents.data.retrofit.ApiConfig
import com.ftthreign.dicodingevents.databinding.FragmentHomeBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var finishedAdapter: EventAdapter
    private lateinit var upcomingAdapter : EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.dicodingEvent
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        setRv()
        findEvent(0)
        findEvent(1)
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

    private fun findEvent(event: Int) {
        val client = ApiConfig.getApiService().getEvents(event)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null) {
                        val eventList: List<EventOverview> = responseBody.listEvents.map {
                            EventOverview(it.mediaCover, it.name)
                        }.take(5)
                        Log.d("UpcomingEvent", "image url ${responseBody.listEvents.map {
                            it.mediaCover
                        }}")
                        if(event == 1) {
                            finishedAdapter.submitList(eventList)
                        } else if(event == 0) {
                            upcomingAdapter.submitList(eventList)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}