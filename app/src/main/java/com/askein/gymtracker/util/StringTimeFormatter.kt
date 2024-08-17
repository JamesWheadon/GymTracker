package com.askein.gymtracker.util

import com.askein.gymtracker.R

fun getTimeStringResourceFromSeconds(seconds: Int): StringResourceArguments {
    return when {
        seconds >= 3600 -> {
            StringResourceArguments(
                R.string.display_hours,
                listOf(
                    (seconds / 3600).toString(),
                    String.format("%02d", (seconds % 3600) / 60),
                    String.format("%02d", seconds % 60)
                )
            )
        }
        seconds >= 60 -> {
            StringResourceArguments(
                R.string.display_minutes,
                listOf(
                    String.format("%02d", (seconds % 3600) / 60),
                    String.format("%02d", seconds % 60)
                )
            )
        }
        else -> {
            StringResourceArguments(
                R.string.display_seconds,
                listOf(String.format("%02d", seconds % 60))
            )
        }
    }
}

data class StringResourceArguments(
    val resourceId: Int,
    val arguments: List<String>
)
