package com.ftthreign.dicodingevents.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ftthreign.dicodingevents.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao() : EventDao

    companion object {
        @Volatile
        private var INSTANCE :EventDatabase? = null

        fun getDatabase(context: Context) : EventDatabase {
            if(INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, EventDatabase::class.java, "dicoding_event_database").build()
                }
            }
            return INSTANCE as EventDatabase
        }
    }
}