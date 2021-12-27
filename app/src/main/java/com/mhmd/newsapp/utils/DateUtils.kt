package com.mhmd.newsapp.utils

import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private val mdf = SimpleDateFormat("MMM d, yyyy - HH:mm")

    fun longToDate(long: Long): Date {
        return Date(long)
    }

    fun dateToLong(date: Date): Long {
        return date.time / 1000 // return seconds
    }

    // Ex: November 4, 2021
    fun dateToString(date: Date): String {
        return mdf.format(date)
    }

    fun stringToDate(string: String): Date {
        return sdf.parse(string)
            ?: throw NullPointerException("Could not convert date string to Date object.")
    }

    fun createTimestamp(): Date {
        return Date()
    }
}
