package com.ftthreign.dicodingevents.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftthreign.dicodingevents.data.response.EventOverview
import com.ftthreign.dicodingevents.data.response.EventsResponse
import com.ftthreign.dicodingevents.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _upcomingEvent = MutableLiveData<List<EventOverview>>()
    private val _finishedEvent = MutableLiveData<List<EventOverview>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()

    val upcomingEvent : LiveData<List<EventOverview>> = _upcomingEvent
    val finishedEvent : LiveData<List<EventOverview>> = _finishedEvent
    val isLoading : LiveData<Boolean> = _isLoading
    val errorMessage : LiveData<String> = _errorMessage

    fun findEvent(event: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(event)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null) {
                        val eventList: List<EventOverview> = responseBody.listEvents.map {
                            EventOverview(it.mediaCover, it.name, it.id)
                        }.take(5)
                        Log.d("UpcomingEvent", "image url ${responseBody.listEvents.map {
                            it.mediaCover
                        }}")
                        if(event == 1) {
                            _finishedEvent.value = eventList
                        } else if(event == 0) {
                            _upcomingEvent.value = eventList
                        }
                    }
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }

}