package com.ftthreign.dicodingevents.ui.upcoming_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.data.response.EventsResponse
import com.ftthreign.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEventViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _upcomingEvent = MutableLiveData<List<EventOverview>>()
    private val _errorMessage = MutableLiveData<String>()

    val isLoading : LiveData<Boolean> = _isLoading
    val upcomingEvent : LiveData<List<EventOverview>> = _upcomingEvent
    val errorMessage : LiveData<String> = _errorMessage

    fun findUpcomingEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null) {
                        val eventList: List<EventOverview> = responseBody.listEvents.map {event->
                            EventOverview(event.mediaCover, event.name, event.id)
                        }
                        _upcomingEvent.value = eventList
                    }
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _errorMessage.value = t.message
            }
        })
    }
}