package com.askein.gymtracker

import android.app.Application
import com.askein.gymtracker.data.AppContainer
import com.askein.gymtracker.data.AppDataContainer

class GymTrackerApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
