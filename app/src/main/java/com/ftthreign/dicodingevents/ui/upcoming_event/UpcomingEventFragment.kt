package com.ftthreign.dicodingevents.ui.upcoming_event

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
import com.ftthreign.dicodingevents.databinding.FragmentUpcomingEventBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEventFragment : Fragment() {

    private var _binding: FragmentUpcomingEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: EventAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val upcomingEventViewModel =
//            ViewModelProvider(this).get(UpcomingEventViewModel::class.java)

        _binding = FragmentUpcomingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.titleUpcoming
//        upcomingEventViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        setRv()
        findUpcomingEvent()

        return root
    }

    private fun setRv() {
        adapter = EventAdapter()
        binding.upcomingEvent.layoutManager = LinearLayoutManager(context)
        binding.upcomingEvent.adapter = adapter
    }

    private fun findUpcomingEvent() {
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventsResponse>{
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null) {
                        val eventList: List<EventOverview> = responseBody.listEvents.map {
                            EventOverview(it.mediaCover, it.name)
                        }
                        Log.d("UpcomingEvent", "image url ${responseBody.listEvents.map { 
                            it.mediaCover
                        }}")
                        adapter.submitList(eventList)
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