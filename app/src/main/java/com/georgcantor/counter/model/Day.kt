package com.georgcantor.counter.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.georgcantor.counter.util.CalendarConverter
import java.util.*

@Entity(tableName = "day_counts")
data class Day(
    @PrimaryKey
    val id: Int,
    val hours: Int,
    @TypeConverters(CalendarConverter::class)
    val calendar: Calendar
)