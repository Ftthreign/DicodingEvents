package com.ftthreign.dicodingevents.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ftthreign.dicodingevents.data.local.entity.EventEntity
import com.ftthreign.dicodingevents.data.remote.EventRepository
import com.ftthreign.dicodingevents.helper.Result
import com.ftthreign.dicodingevents.ui.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingPreferences: SettingPreferences,
    private val eventRepository: EventRepository
) : ViewModel() {
    fun getThemeSettings() : LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode : Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkMode)
        }
    }
    fun getNotificationSetting(): LiveData<Boolean> {
        return settingPreferences.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveNotificationSetting(isActive)
        }
    }
    fun searchEvent(query : String) : LiveData<Result<List<EventEntity>>> {
        return eventRepository.searchEvent(query)
    }

    fun getUpcomingEvent(): LiveData<Result<List<EventEntity>>> {
        return eventRepository.getAllEvents(1)
    }

    fun getFinishedEvent() : LiveData<Result<List<EventEntity>>> {
        return eventRepository.getAllEvents(0)
    }

    fun getFavouriteEvent() : LiveData<List<EventEntity>> {
        return eventRepository.getFavouriteEvent()
    }

    fun addEventToFavourite(event : EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavouriteEvent(event, true)
        }
    }

    fun removeEventFromFavourite(event : EventEntity) {
        viewModelScope.launch {
            eventRepository.setFavouriteEvent(event, false)
        }
    }
}