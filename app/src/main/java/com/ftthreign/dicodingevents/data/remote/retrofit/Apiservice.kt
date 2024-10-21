package com.ftthreign.dicodingevents.data.remote.retrofit

import com.ftthreign.dicodingevents.data.remote.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiservice {
    @GET("events")
    suspend fun getEvents(
        @Query("active")
        active : Int
    ) : EventsResponse

    @GET("events")
    suspend fun getUpdatedEvent(
        @Query("active")
        active: Int = -1,
        @Query("limit")
        limit : Int = 40
    ) : EventsResponse

}