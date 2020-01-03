package com.georgcantor.counter.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: Day): Long

    @Query("UPDATE days SET hours = :hours WHERE id LIKE :id")
    suspend fun updateById(id: String, hours: Float)

    @Query("SELECT * FROM days WHERE id LIKE :id")
    suspend fun getById(id: String): List<Day>

    @Query("SELECT * FROM days ORDER by id")
    suspend fun getAll(): List<Day>

    @Query("DELETE FROM days WHERE id LIKE :id")
    suspend fun deleteById(id: String)

}