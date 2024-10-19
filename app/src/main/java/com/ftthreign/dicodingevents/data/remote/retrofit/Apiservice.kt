package com.ftthreign.dicodingevents.data.remote.retrofit

import com.ftthreign.dicodingevents.data.remote.response.EventsResponse
import com.ftthreign.dicodingevents.data.remote.response.DetailEventsResponse
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
        id: Int
    ): Call<DetailEventsResponse>

    @GET("events")
    fun searchEvents(
        @Query("active")
        active: Int,
        @Query("q")
        query: String
    ) : Call<EventsResponse>
}