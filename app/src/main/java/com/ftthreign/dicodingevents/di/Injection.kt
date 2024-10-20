package com.ftthreign.dicodingevents.di

import android.content.Context
import com.ftthreign.dicodingevents.data.local.room.EventDatabase
import com.ftthreign.dicodingevents.data.remote.EventRepository
import com.ftthreign.dicodingevents.data.remote.retrofit.ApiConfig
import com.ftthreign.dicodingevents.helper.AppExecutors

object Injection {
    fun provideRepository(context: Context) : EventRepository {
        val apiservice = ApiConfig.getApiService()
        val db = EventDatabase.getDatabase(context)
        val dao = db.eventDao()
        val executor = AppExecutors()
        return EventRepository.getInstance(apiservice, dao, executor)
    }
}