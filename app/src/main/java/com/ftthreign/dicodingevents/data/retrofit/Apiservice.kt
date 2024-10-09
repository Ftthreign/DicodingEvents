package com.ftthreign.dicodingevents.data.retrofit

import com.ftthreign.dicodingevents.data.response.EventsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apiservice {
    @GET("events")
    fun getEvents(
        @Query("active")
        active : Int
    ) : Call<EventsResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id")
        id: String
    ): Call<EventsResponse>
}