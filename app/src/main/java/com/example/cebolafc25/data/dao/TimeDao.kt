package com.example.cebolafc25.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cebolafc25.data.model.TimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(time: TimeEntity)

    @Update
    suspend fun updateTime(time: TimeEntity)

    @Query("SELECT * FROM times WHERE nome = :nomeTime")
    fun getTimeByName(nomeTime: String): Flow<TimeEntity?>

    @Query("SELECT * FROM times ORDER BY nome ASC")
    fun getAllTimes(): Flow<List<TimeEntity>>

    @Query("DELETE FROM times WHERE nome = :nomeTime")
    suspend fun deleteTimeByName(nomeTime: String)
}