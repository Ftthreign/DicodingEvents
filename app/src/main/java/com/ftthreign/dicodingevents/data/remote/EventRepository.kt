package com.ftthreign.dicodingevents.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.ftthreign.dicodingevents.data.local.entity.EventEntity
import com.ftthreign.dicodingevents.data.local.room.EventDao
import com.ftthreign.dicodingevents.data.remote.response.EventsResponse
import com.ftthreign.dicodingevents.data.remote.retrofit.Apiservice
import com.ftthreign.dicodingevents.helper.AppExecutors
import com.ftthreign.dicodingevents.helper.Result
import kotlinx.coroutines.withContext
import kotlinx.coroutines.asCoroutineDispatcher

class EventRepository private constructor(
    private val apiService: Apiservice,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getAllEvents(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active)

            val data = response.listEvents
            val eventList = data.map { event ->
                val isFinished = active == 0
                val isUpcoming = active == 1
                val isFavourite = event.name.let {
                    eventDao.isEventFavourite(it)
                }
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    isFavourite,
                    isFinished,
                    isUpcoming
                )
            }

            eventDao.insertEventsData(eventList)
            emit(Result.Success(eventList))

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavouriteEvent(): LiveData<List<EventEntity>> {
        return eventDao.getFavouriteEvent()
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val dataLocal = eventDao.searchFinishedEvent(query).map { eventList ->
                if (eventList.isNotEmpty()) {
                    Result.Success(eventList)
                } else {
                    Result.Error("No events found for the query: $query")
                }
            }
            emitSource(dataLocal)
        } catch (exception: Exception) {
            emit(Result.Error(exception.message.toString()))
        }
    }

    suspend fun setFavouriteEvent(event: EventEntity, favourite: Boolean) {
        event.isFavourite = favourite
        withContext(appExecutors.diskIO.asCoroutineDispatcher()) {
            eventDao.updateEventsData(event)
        }
    }

    suspend fun getNearestEvent() : EventsResponse? {
        val getEvent =  try {
            apiService.getUpdatedEvent(active = -1, limit = 1)
        } catch (e: Exception) {
            null
        }
        return getEvent
    }

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(
            apiService: Apiservice,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: EventRepository(apiService, eventDao, appExecutors)
        }.also { INSTANCE = it }
    }
}
