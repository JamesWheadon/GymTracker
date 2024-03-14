package com.example.gymtracker

import androidx.test.platform.app.InstrumentationRegistry


fun getResourceString(resourceId: Int) = InstrumentationRegistry.getInstrumentation().targetContext.getString(
    resourceId
)
