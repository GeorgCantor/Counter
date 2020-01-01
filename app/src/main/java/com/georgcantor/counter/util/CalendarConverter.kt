package com.georgcantor.counter.util

import androidx.room.TypeConverter
import java.util.*

class CalendarConverter {

    @TypeConverter
    fun toCalendar(millis: Long): Calendar = Calendar.getInstance().apply { timeInMillis = millis }

    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long = calendar.time.time

}