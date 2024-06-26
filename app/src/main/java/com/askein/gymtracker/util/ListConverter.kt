package com.askein.gymtracker.util

import androidx.room.TypeConverter


class ListConverter {
    @TypeConverter
    fun doubleArrayFromString(value: String?): List<Double>? {
        return value?.split("|")?.map { it.toDouble() }
    }

    @TypeConverter
    fun doubleArrayToString(list: List<Double>?): String? {
        return list?.joinToString("|")
    }

    @TypeConverter
    fun intArrayFromString(value: String?): List<Int>? {
        return value?.split("|")?.map { it.toInt() }
    }

    @TypeConverter
    fun intArrayToString(list: List<Int>?): String? {
        return list?.joinToString("|")
    }
}
