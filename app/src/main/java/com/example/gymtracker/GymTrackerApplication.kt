package com.example.gymtracker

import android.app.Application
import com.example.gymtracker.data.AppContainer
import com.example.gymtracker.data.AppDataContainer

class GymTrackerApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
