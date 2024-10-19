package com.ftthreign.dicodingevents.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ftthreign.dicodingevents.ui.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingPreferences: SettingPreferences
) : ViewModel() {
    fun getThemeSettings() : LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode : Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkMode)
        }
    }
}