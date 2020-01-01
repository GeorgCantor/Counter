package com.georgcantor.counter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class Day(
    @PrimaryKey
    val id: String,
    val hours: Int
//    @TypeConverters(CalendarConverter::class)
//    val calendar: Calendar
)