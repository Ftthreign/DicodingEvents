package com.ftthreign.dicodingevents.ui.finished_event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.data.response.EventsResponse
import com.ftthreign.dicodingevents.data.retrofit.ApiConfig
import com.ftthreign.dicodingevents.databinding.FragmentFinishedEventBinding
import com.ftthreign.dicodingevents.ui.adapter.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val finishedEventViewModel =
//            ViewModelProvider(this).get(FinishedEventViewModel::class.java)

        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textView2
//        finishedEventViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        setRv()
        findFinishedEvent()


        return root
    }

    private fun setRv() {
        eventAdapter = EventAdapter()
        binding.finishedEvent.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.finishedEvent.adapter = eventAdapter
    }

    private fun findFinishedEvent() {
        val client = ApiConfig.getApiService().getEvents(0)
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
                        }
                        Log.d("UpcomingEvent", "image url ${responseBody.listEvents.map {
                            it.mediaCover
                        }}")
                        eventAdapter.submitList(eventList)
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