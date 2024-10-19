package com.ftthreign.dicodingevents.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftthreign.dicodingevents.ui.SettingPreferences
import com.ftthreign.dicodingevents.ui.dataStore

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val preference : SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(preference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class data : ${modelClass.name} please create new ViewModel at factory")
        }
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                val preference = SettingPreferences.getInstance(context.dataStore)
                instance ?:ViewModelFactory(preference)
            }.also { instance =  it }
    }
}