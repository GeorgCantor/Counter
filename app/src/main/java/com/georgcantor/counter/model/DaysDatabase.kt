package com.georgcantor.counter.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Day::class],
    version = 1,
    exportSchema = false
)
abstract class DaysDatabase : RoomDatabase() {

    companion object {
        private const val dbName = "days_db"

        fun buildDefault(context: Context) = Room.databaseBuilder(
            context,
            DaysDatabase::class.java,
            dbName
        )
            .build()
    }

    abstract fun dao(): DaysDao

}