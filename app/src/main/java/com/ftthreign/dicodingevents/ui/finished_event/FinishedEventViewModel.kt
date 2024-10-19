package com.ftthreign.dicodingevents.ui.finished_event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftthreign.dicodingevents.data.remote.response.EventOverview
import com.ftthreign.dicodingevents.data.remote.response.EventsResponse
import com.ftthreign.dicodingevents.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedEventViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    private val _finishedEvent = MutableLiveData<List<EventOverview>>()
    private val _errorMessage = MutableLiveData<String>()
    val isLoading : LiveData<Boolean> = _isLoading
    val finishedEvent : LiveData<List<EventOverview>> = _finishedEvent
    val errorMessage : LiveData<String> = _errorMessage

    fun findFinishedEvent(query : String? = null) {
        _isLoading.value = true
        val client = if(query.isNullOrEmpty()) {
            ApiConfig.getApiService().getEvents(0)
        } else {
            ApiConfig.getApiService().searchEvents(-1, query)
        }
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null) {
                        val eventList: List<EventOverview> = responseBody.listEvents.map { event ->
                            EventOverview(event.mediaCover, event.name, event.id, event.summary, event.imageLogo, event.cityName)
                        }
                        Log.d("UpcomingEvent", "image url ${responseBody.listEvents.map {
                            it.mediaCover
                        }}")
                        _finishedEvent.value = eventList
                    }
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _errorMessage.value = t.message
            }

        })
    }

}