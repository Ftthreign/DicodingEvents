package com.ftthreign.dicodingevents.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftthreign.dicodingevents.data.response.Event
import com.ftthreign.dicodingevents.data.retrofit.ApiConfig
import com.ftthreign.dicodingevents.data.response.DetailEventsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _eventDetail = MutableLiveData<Event>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()
    val isLoading : LiveData<Boolean> = _isLoading
    val eventDetail: LiveData<Event> = _eventDetail
    val errorMessage : LiveData<String> = _errorMessage

    fun getEventDetail(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<DetailEventsResponse> {
            override fun onResponse(call: Call<DetailEventsResponse>, response: Response<DetailEventsResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _eventDetail.postValue(response.body()?.event)
                }
            }

            override fun onFailure(call: Call<DetailEventsResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }



}