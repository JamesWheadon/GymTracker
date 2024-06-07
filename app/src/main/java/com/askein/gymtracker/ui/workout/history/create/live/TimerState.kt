package com.askein.gymtracker.ui.workout.history.create.live

import java.time.Instant

data class TimerState(
    val currentTime: Int = 0,
    val timerRunning: Boolean = false,
    val endTime: Instant = Instant.now()
)
